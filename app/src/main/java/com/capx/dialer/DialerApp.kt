package com.capx.dialer

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capx.dialer.core.ui.components.DialerBottomBar
import com.capx.dialer.core.ui.components.DialerButton
import com.capx.dialer.core.ui.components.DialerTab
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
    Manifest.permission.RECORD_AUDIO
)

@Composable
fun DialerApp() {
    val context = LocalContext.current
    var permissionsGranted by remember {
        mutableStateOf(
            REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        // If all are granted, proceed
        permissionsGranted = result.values.all { it }
    }

    if (!permissionsGranted) {
        // Permission Screen
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Permissions Required",
                    style = DialerTheme.typography.title,
                    color = DialerTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The dialer needs access to your contacts, call log, and microphone to work properly.",
                    style = DialerTheme.typography.body,
                    color = DialerTheme.colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                DialerButton(
                    text = "Grant Permissions",
                    onClick = { launcher.launch(REQUIRED_PERMISSIONS) }
                )
            }
        }
        return
    }

    // Main App
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        DialerTab(DialerIcons.Keypad, DialerIcons.Keypad, "Dialpad"),
        DialerTab(DialerIcons.Clock, DialerIcons.ClockFilled, "Recents"),
        DialerTab(DialerIcons.Contacts, DialerIcons.ContactsFilled, "Contacts"),
        DialerTab(DialerIcons.Mic, DialerIcons.MicFilled, "Recordings")
    )

    Scaffold(
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
            NavHost(
                navController = navController,
                startDestination = "dialpad"
            ) {
                composable("dialpad") {
                    DialpadScreen(
                        onNavigateToCall = { /* Handle navigation to InCall UI if not using service */ },
                        onShowSnackbar = { /* Handle snackbar */ }
                    )
                }
                composable("recents") {
                    RecentsScreen(
                        onCall = { /* Handle call */ }
                    )
                }
                composable("contacts") {
                    ContactsScreen(
                        onNavigateToDetail = { /* Navigate */ },
                        onCall = { /* Handle call */ }
                    )
                }
                composable("recordings") {
                    RecordingsScreen()
                }
            }
        }
    }
}
