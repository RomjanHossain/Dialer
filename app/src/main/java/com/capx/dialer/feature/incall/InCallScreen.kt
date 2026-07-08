package com.capx.dialer.feature.incall

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.ui.components.DialerButton

@Composable
fun InCallScreen(
    viewModel: InCallViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is InCallUiEvent.Dismiss -> onDismiss()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val title = when (val s = state.callState) {
            is CallState.Active -> s.contactName ?: s.number
            is CallState.Dialing -> "Calling ${s.contactName ?: s.number}..."
            is CallState.Ringing -> "Incoming: ${s.contactName ?: s.number}"
            is CallState.OnHold -> "On Hold: ${s.number}"
            is CallState.Disconnected -> "Call Ended"
            is CallState.Idle -> "No Active Call"
        }
        
        Text(text = title, modifier = Modifier.padding(bottom = 64.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DialerButton(text = "Mute", onClick = { viewModel.toggleMute() })
            DialerButton(text = "Speaker", onClick = { viewModel.toggleSpeaker() })
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        DialerButton(text = "End Call", onClick = { viewModel.endCall() })
    }
}
