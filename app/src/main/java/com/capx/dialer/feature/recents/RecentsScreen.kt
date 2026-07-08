package com.capx.dialer.feature.recents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RecentsScreen(
    viewModel: RecentsViewModel = hiltViewModel(),
    onCall: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RecentsUiEvent.CallNumber -> onCall(event.number)
                is RecentsUiEvent.ShowSnackbar -> { /* Show snackbar */ }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(state.calls) { call ->
                    RecentCallItem(
                        name = call.contactName ?: call.number,
                        time = call.timestamp.toString(), // format properly in real app
                        onClick = { viewModel.onCallClick(call.number) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecentCallItem(name: String, time: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(text = name)
        Text(text = time)
    }
}
