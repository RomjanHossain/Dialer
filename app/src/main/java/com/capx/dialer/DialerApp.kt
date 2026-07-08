package com.capx.dialer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capx.dialer.feature.contacts.ContactsScreen
import com.capx.dialer.feature.dialpad.DialpadScreen
import com.capx.dialer.feature.recents.RecentsScreen
import com.capx.dialer.feature.recordings.RecordingsScreen

@Composable
fun DialerApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // Placeholder for DialerBottomBar
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
