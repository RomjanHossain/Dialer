package com.capx.dialer.feature.calllog

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.domain.model.CallType
import com.capx.dialer.core.ui.components.DialerAvatar
import com.capx.dialer.core.ui.components.DialerDivider
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DialerTheme
import com.capx.dialer.core.ui.util.ContactIntents
import com.capx.dialer.core.ui.util.DateFormat

@Composable
fun CallLogDetailScreen(
    onBack: () -> Unit,
    onCall: (String) -> Unit,
    viewModel: CallLogDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = DialerTheme.colors
    val context = LocalContext.current
    val title = state.name?.takeIf { it.isNotBlank() } ?: state.number

    Column(modifier = Modifier.fillMaxSize().background(colors.background)) {
        // Top bar with back
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            IconCircle(icon = DialerIcons.ChevronLeft, description = "Back", onClick = onBack)
        }

        // Header card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            DialerAvatar(name = state.name.orEmpty(), number = state.number, size = 96.dp)
            Spacer(Modifier.height(12.dp))
            androidx.compose.material3.Text(
                text = title,
                style = DialerTheme.typography.headline.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1
            )
            if (state.name != null) {
                androidx.compose.material3.Text(
                    text = state.number,
                    style = DialerTheme.typography.body.copy(color = colors.textSecondary)
                )
            }
            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                HeaderAction(
                    icon = DialerIcons.Phone,
                    label = "Call",
                    tint = colors.callGreen,
                    onClick = { onCall(state.number) }
                )
                HeaderAction(
                    icon = if (state.name == null) DialerIcons.AddCall else DialerIcons.Contacts,
                    label = if (state.name == null) "Add" else "Info",
                    tint = colors.primary,
                    onClick = { ContactIntents.viewOrCreate(context, state.number) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        DialerDivider()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.calls, key = { it.id }) { call ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    androidx.compose.foundation.Image(
                        imageVector = directionIcon(call.type),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(directionColor(call.type, colors))
                    )
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        androidx.compose.material3.Text(
                            text = typeLabel(call.type),
                            style = DialerTheme.typography.body.copy(color = colors.textPrimary)
                        )
                        androidx.compose.material3.Text(
                            text = DateFormat.relative(call.timestamp),
                            style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary)
                        )
                    }
                    val dur = DateFormat.duration(call.duration)
                    if (dur.isNotBlank()) {
                        androidx.compose.material3.Text(
                            text = dur,
                            style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary)
                        )
                    }
                }
                DialerDivider(startIndent = 52.dp)
            }
        }
    }
}

@Composable
private fun HeaderAction(icon: ImageVector, label: String, tint: Color, onClick: () -> Unit) {
    val colors = DialerTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.14f))
                .clickable(onClick = onClick)
        ) {
            androidx.compose.foundation.Image(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(tint)
            )
        }
        Spacer(Modifier.height(6.dp))
        androidx.compose.material3.Text(
            text = label,
            style = DialerTheme.typography.caption.copy(color = colors.textSecondary)
        )
    }
}

@Composable
private fun IconCircle(icon: ImageVector, description: String, onClick: () -> Unit) {
    val colors = DialerTheme.colors
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        androidx.compose.foundation.Image(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(colors.textPrimary)
        )
    }
}

private fun typeLabel(type: CallType): String = when (type) {
    CallType.INCOMING -> "Incoming call"
    CallType.OUTGOING -> "Outgoing call"
    CallType.MISSED -> "Missed call"
    CallType.REJECTED -> "Declined call"
    CallType.BLOCKED -> "Blocked call"
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
