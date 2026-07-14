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

@Composable
fun RecentsScreen(
    viewModel: RecentsViewModel = hiltViewModel(),
    onCall: (String) -> Unit,
    onOpenCallLog: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = DialerTheme.colors
    val context = androidx.compose.ui.platform.LocalContext.current

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
                        SwipeableRecentRow(
                            onCall = { viewModel.onCallClick(group.number) },
                            onMessage = {
                                com.capx.dialer.core.ui.util.ContactIntents.message(context, group.number)
                            }
                        ) {
                            RecentRow(
                                name = group.name?.takeIf { it.isNotBlank() },
                                number = group.number,
                                type = group.type,
                                count = group.count,
                                subtitle = group.subtitle,
                                onCall = { viewModel.onCallClick(group.number) },
                                onOpenDetail = { onOpenCallLog(group.number) },
                                onOpenContact = {
                                    com.capx.dialer.core.ui.util.ContactIntents.viewOrCreate(context, group.number)
                                }
                            )
                        }
                        DialerDivider(startIndent = 80.dp)
                    }
                }
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableRecentRow(
    onCall: () -> Unit,
    onMessage: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = DialerTheme.colors
    val dismissState = androidx.compose.material3.rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                // Swipe LEFT (end -> start) calls; swipe RIGHT (start -> end) messages.
                androidx.compose.material3.SwipeToDismissBoxValue.EndToStart -> onCall()
                androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd -> onMessage()
                androidx.compose.material3.SwipeToDismissBoxValue.Settled -> Unit
            }
            // Never actually dismiss — just trigger the action and snap back.
            false
        },
        // Require dragging at least halfway across the row so ordinary
        // (vertical) scrolling never accidentally triggers an action.
        positionalThreshold = { totalDistance -> totalDistance * 0.5f }
    )

    androidx.compose.material3.SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val isCall = direction == androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
            // Colour the revealed background by action: green for call, accent for message.
            val bg = when (direction) {
                androidx.compose.material3.SwipeToDismissBoxValue.EndToStart -> colors.callGreen
                androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd -> colors.secondary
                androidx.compose.material3.SwipeToDismissBoxValue.Settled -> Color.Transparent
            }
            val icon = if (isCall) DialerIcons.Phone else DialerIcons.Message
            // Call reveals from the right (end); message reveals from the left (start).
            val alignment = if (isCall) Alignment.CenterEnd else Alignment.CenterStart
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .padding(horizontal = 28.dp)
            ) {
                if (direction != androidx.compose.material3.SwipeToDismissBoxValue.Settled) {
                    androidx.compose.foundation.Image(
                        imageVector = icon,
                        contentDescription = if (isCall) "Call" else "Message",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        },
        content = { content() }
    )
}

@Composable
private fun RecentRow(
    name: String?,
    number: String,
    type: CallType,
    count: Int,
    subtitle: String,
    onCall: () -> Unit,
    onOpenDetail: () -> Unit,
    onOpenContact: () -> Unit
) {
    val colors = DialerTheme.colors
    val isMissed = type == CallType.MISSED || type == CallType.REJECTED
    val titleColor = if (isMissed) colors.callRed else colors.textPrimary
    val display = (name ?: number).let { if (count > 1) "$it ($count)" else it }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenDetail)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        DialerAvatar(
            name = name.orEmpty(),
            number = number,
            size = 48.dp,
            modifier = Modifier.clip(CircleShape).clickable(onClick = onOpenContact)
        )
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
                    text = subtitle,
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
