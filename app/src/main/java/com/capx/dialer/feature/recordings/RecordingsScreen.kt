package com.capx.dialer.feature.recordings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.domain.model.Recording
import com.capx.dialer.core.ui.components.DialerAvatar
import com.capx.dialer.core.ui.components.DialerDivider
import com.capx.dialer.core.ui.components.ScreenHeader
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DialerTheme
import com.capx.dialer.core.ui.util.DateFormat

@Composable
fun RecordingsScreen(
    viewModel: RecordingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = DialerTheme.colors

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RecordingsUiEvent.ShowSnackbar -> Unit
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(colors.background)) {
        ScreenHeader(title = "Recordings")

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            state.recordings.isEmpty() -> EmptyState()

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.recordings, key = { it.id }) { recording ->
                        RecordingRow(recording = recording, onClick = { /* play */ })
                        DialerDivider(startIndent = 80.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordingRow(recording: Recording, onClick: () -> Unit) {
    val colors = DialerTheme.colors
    val title = recording.contactName?.takeIf { it.isNotBlank() }
        ?: recording.contactNumber
        ?: "Unknown"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        DialerAvatar(name = recording.contactName.orEmpty(), number = recording.contactNumber.orEmpty(), size = 48.dp)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(
                text = title,
                style = DialerTheme.typography.body.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )
            androidx.compose.material3.Text(
                text = buildString {
                    append(DateFormat.relative(recording.timestamp))
                    val dur = DateFormat.duration(recording.duration)
                    if (dur.isNotBlank()) {
                        if (isNotEmpty()) append(" · ")
                        append(dur)
                    }
                },
                style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary),
                maxLines = 1
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(colors.primary.copy(alpha = 0.14f))
                .clickable(onClick = onClick)
        ) {
            androidx.compose.foundation.Image(
                imageVector = DialerIcons.Play,
                contentDescription = "Play recording",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(colors.primary)
            )
        }
    }
}

@Composable
private fun EmptyState() {
    val colors = DialerTheme.colors
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.foundation.Image(
            imageVector = DialerIcons.Mic,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            colorFilter = ColorFilter.tint(colors.textTertiary)
        )
        Spacer(Modifier.height(12.dp))
        androidx.compose.material3.Text(
            text = "No recordings yet",
            style = DialerTheme.typography.title.copy(color = colors.textPrimary)
        )
        Spacer(Modifier.height(4.dp))
        androidx.compose.material3.Text(
            text = "Recorded calls will appear here.",
            style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary)
        )
    }
}
