package com.capx.dialer

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capx.dialer.core.telecom.DefaultDialer
import com.capx.dialer.core.ui.components.DialerBottomBar
import com.capx.dialer.core.ui.components.DialerButton
import com.capx.dialer.core.ui.components.DialerTab
import com.capx.dialer.core.ui.components.SimPickerSheet
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DialerTheme
import com.capx.dialer.feature.contacts.ContactsScreen
import com.capx.dialer.feature.dialpad.DialpadScreen
import com.capx.dialer.feature.recents.RecentsScreen
import com.capx.dialer.feature.recordings.RecordingsScreen

private val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.WRITE_CONTACTS,
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.WRITE_CALL_LOG,
    Manifest.permission.CALL_PHONE,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.RECORD_AUDIO
)

@Composable
fun DialerApp(mainViewModel: MainViewModel = hiltViewModel()) {
    val context = LocalContext.current

    var permissionsGranted by remember {
        mutableStateOf(
            REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        )
    }
    var isDefaultDialer by remember { mutableStateOf(DefaultDialer.isDefault(context)) }
    var skippedDefaultPrompt by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionsGranted = result.values.all { it }
        if (permissionsGranted) mainViewModel.refreshSims()
    }

    val defaultDialerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        isDefaultDialer = DefaultDialer.isDefault(context)
    }

    // Pre-load SIM accounts once permissions are already present.
    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) mainViewModel.refreshSims()
    }

    when {
        !permissionsGranted -> SetupScreen(
            title = "Permissions Required",
            message = "The dialer needs access to your contacts, call log, phone, and microphone to work properly.",
            buttonText = "Grant Permissions",
            onButtonClick = { permissionLauncher.launch(REQUIRED_PERMISSIONS) }
        )

        !isDefaultDialer && !skippedDefaultPrompt -> SetupScreen(
            title = "Set as Default Phone App",
            message = "To show this app's own calling screen and place calls reliably, make it your default phone app.",
            buttonText = "Set as Default",
            onButtonClick = {
                DefaultDialer.createRequestIntent(context)?.let { defaultDialerLauncher.launch(it) }
            },
            secondaryText = "Not now",
            onSecondaryClick = { skippedDefaultPrompt = true }
        )
        else -> MainShell(mainViewModel = mainViewModel)
    }
}

@Composable
private fun MainShell(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val callState by mainViewModel.state.collectAsStateWithLifecycle()

    val tabs = listOf(
        DialerTab(DialerIcons.Keypad, DialerIcons.Keypad, "Dialpad"),
        DialerTab(DialerIcons.Clock, DialerIcons.ClockFilled, "Recents"),
        DialerTab(DialerIcons.Contacts, DialerIcons.ContactsFilled, "Contacts"),
        DialerTab(DialerIcons.Mic, DialerIcons.MicFilled, "Recordings")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = DialerTheme.colors.background,
            bottomBar = {
                DialerBottomBar(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { index ->
                        selectedIndex = index
                        val route = when (index) {
                            0 -> "dialpad"
                            1 -> "recents"
                            2 -> "contacts"
                            3 -> "recordings"
                            else -> "dialpad"
                        }
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                NavHost(navController = navController, startDestination = "dialpad") {
                    composable("dialpad") {
                        DialpadScreen(
                            onPlaceCall = mainViewModel::requestCall,
                            onShowSnackbar = { }
                        )
                    }
                    composable("recents") {
                        RecentsScreen(onCall = mainViewModel::requestCall)
                    }
                    composable("contacts") {
                        ContactsScreen(onCall = mainViewModel::requestCall)
                    }
                    composable("recordings") {
                        RecordingsScreen()
                    }
                }
            }
        }

        // Fast in-app SIM chooser, overlaid above everything.
        SimPickerSheet(
            visible = callState.pendingCallNumber != null,
            sims = callState.sims,
            onSelect = mainViewModel::onSimChosen,
            onDismiss = mainViewModel::dismissSimPicker
        )
    }
}

@Composable
private fun SetupScreen(
    title: String,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null
) {
    val colors = DialerTheme.colors
    Box(
        modifier = Modifier.fillMaxSize().background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            androidx.compose.material3.Text(
                text = title,
                style = DialerTheme.typography.title,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Text(
                text = message,
                style = DialerTheme.typography.body,
                color = colors.textSecondary,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            DialerButton(text = buttonText, onClick = onButtonClick)
            if (secondaryText != null && onSecondaryClick != null) {
                Spacer(modifier = Modifier.height(12.dp))
                androidx.compose.material3.Text(
                    text = secondaryText,
                    style = DialerTheme.typography.body,
                    color = colors.textSecondary,
                    modifier = Modifier
                        .clip(DialerTheme.shapes.pill)
                        .clickable(onClick = onSecondaryClick)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
