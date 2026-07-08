package com.capx.dialer.feature.dialpad

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.ui.components.DialerButton
import com.capx.dialer.core.ui.theme.DialerTheme

@Composable
fun DialpadScreen(
    viewModel: DialpadViewModel = hiltViewModel(),
    onNavigateToCall: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is DialpadUiEvent.NavigateToCall -> onNavigateToCall()
                is DialpadUiEvent.ShowSnackbar -> onShowSnackbar(event.message)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Suggested Contacts
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.suggestedContacts) { contact ->
                Text(
                    text = "${contact.name} (${contact.phoneNumber})",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        // Number Display
        Text(
            text = state.currentNumber,
            style = DialerTheme.typography.display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            textAlign = TextAlign.Center
        )

        // Keypad
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("*", "0", "#")
        )

        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    DialpadButton(
                        text = key,
                        onClick = { viewModel.onDigitPress(key[0]) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            DialerButton(
                text = "Call",
                onClick = viewModel::onCallClick,
                modifier = Modifier.weight(1f)
            )
            
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (state.currentNumber.isNotEmpty()) {
                    Text(
                        text = "Del",
                        modifier = Modifier.clickable { viewModel.onBackspaceClick() }
                    )
                }
            }
        }
    }
}

@Composable
fun DialpadButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = DialerTheme.typography.headline)
    }
}
