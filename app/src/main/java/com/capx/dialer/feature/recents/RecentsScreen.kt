package com.capx.dialer.feature.recents

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.domain.model.CallType
import com.capx.dialer.core.ui.components.DialerAvatar
import com.capx.dialer.core.ui.components.DialerDivider
import com.capx.dialer.core.ui.components.ScreenHeader
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DialerTheme
import com.capx.dialer.core.ui.util.DateFormat

@Composable
fun RecentsScreen(
    viewModel: RecentsViewModel = hiltViewModel(),
    onCall: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = DialerTheme.colors

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RecentsUiEvent.CallNumber -> onCall(event.number)
                is RecentsUiEvent.ShowSnackbar -> Unit
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(colors.background)) {
        ScreenHeader(title = "Recents")

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            state.groups.isEmpty() -> {
                EmptyState()
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.groups, key = { it.id }) { group ->
                        RecentRow(
                            name = group.name?.takeIf { it.isNotBlank() },
                            number = group.number,
                            photoUri = group.photoUri,
                            type = group.type,
                            count = group.count,
                            timeLabel = DateFormat.relative(group.timestamp),
                            durationLabel = DateFormat.duration(group.duration),
                            onCall = { viewModel.onCallClick(group.number) }
                        )
                        DialerDivider(startIndent = 80.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentRow(
    name: String?,
    number: String,
    photoUri: String?,
    type: CallType,
    count: Int,
    timeLabel: String,
    durationLabel: String,
    onCall: () -> Unit
) {
    val colors = DialerTheme.colors
    val isMissed = type == CallType.MISSED || type == CallType.REJECTED
    val titleColor = if (isMissed) colors.callRed else colors.textPrimary
    val display = (name ?: number).let { if (count > 1) "$it ($count)" else it }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCall)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        DialerAvatar(name = name.orEmpty(), number = number, size = 48.dp)
        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(
                text = display,
                style = DialerTheme.typography.body.copy(
                    color = titleColor,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.foundation.Image(
                    imageVector = directionIcon(type),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    colorFilter = ColorFilter.tint(directionColor(type, colors))
                )
                Spacer(Modifier.width(6.dp))
                androidx.compose.material3.Text(
                    text = subtitle(type, timeLabel, durationLabel),
                    style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary),
                    maxLines = 1
                )
            }
        }

        // Call action
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.callGreen.copy(alpha = 0.14f))
                .clickable(onClick = onCall)
        ) {
            androidx.compose.foundation.Image(
                imageVector = DialerIcons.Phone,
                contentDescription = "Call $display",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(colors.callGreen)
            )
        }
    }
}

private fun subtitle(type: CallType, time: String, duration: String): String {
    val label = when (type) {
        CallType.INCOMING -> "Incoming"
        CallType.OUTGOING -> "Outgoing"
        CallType.MISSED -> "Missed"
        CallType.REJECTED -> "Declined"
        CallType.BLOCKED -> "Blocked"
    }
    val parts = buildList {
        add(label)
        if (time.isNotBlank()) add(time)
        if (duration.isNotBlank() && (type == CallType.INCOMING || type == CallType.OUTGOING)) {
            add(duration)
        }
    }
    return parts.joinToString(" · ")
}

private fun directionIcon(type: CallType): ImageVector = when (type) {
    CallType.OUTGOING -> DialerIcons.CallOutgoing
    CallType.MISSED, CallType.REJECTED, CallType.BLOCKED -> DialerIcons.CallMissed
    else -> DialerIcons.CallIncoming
}

private fun directionColor(
    type: CallType,
    colors: com.capx.dialer.core.ui.theme.DialerColorScheme
): Color = when (type) {
    CallType.MISSED, CallType.REJECTED, CallType.BLOCKED -> colors.callRed
    CallType.OUTGOING -> colors.callGreen
    else -> colors.textSecondary
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
            imageVector = DialerIcons.Clock,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            colorFilter = ColorFilter.tint(colors.textTertiary)
        )
        Spacer(Modifier.height(12.dp))
        androidx.compose.material3.Text(
            text = "No recent calls",
            style = DialerTheme.typography.title.copy(color = colors.textPrimary)
        )
        Spacer(Modifier.height(4.dp))
        androidx.compose.material3.Text(
            text = "Calls you make and receive will show up here.",
            style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary)
        )
    }
}
