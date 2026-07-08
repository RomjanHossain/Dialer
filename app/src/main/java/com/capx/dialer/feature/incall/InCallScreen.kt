package com.capx.dialer.feature.incall

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.ui.components.DialerAvatar
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DialerTheme
import com.capx.dialer.core.ui.util.DateFormat
import kotlinx.coroutines.delay

@Composable
fun InCallScreen(
    viewModel: InCallViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = DialerTheme.colors

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is InCallUiEvent.Dismiss -> onDismiss()
            }
        }
    }

    val callState = state.callState
    val name: String
    val number: String
    when (callState) {
        is CallState.Active -> { name = callState.contactName ?: ""; number = callState.number }
        is CallState.Dialing -> { name = callState.contactName ?: ""; number = callState.number }
        is CallState.Ringing -> { name = callState.contactName ?: ""; number = callState.number }
        is CallState.OnHold -> { name = callState.contactName ?: ""; number = callState.number }
        else -> { name = ""; number = "" }
    }

    val status = when (callState) {
        is CallState.Dialing -> "Calling…"
        is CallState.Ringing -> "Incoming call"
        is CallState.OnHold -> "On hold"
        is CallState.Active -> CallTimer(callState.startTime)
        is CallState.Disconnected -> "Call ended"
        is CallState.Idle -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(colors.background, colors.surfaceVariant, colors.background)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Pulsing avatar
            val ringing = callState is CallState.Ringing || callState is CallState.Dialing
            PulsingAvatar(name = name, number = number, pulsing = ringing)

            Spacer(Modifier.height(28.dp))

            androidx.compose.material3.Text(
                text = name.ifBlank { number.ifBlank { "Unknown" } },
                style = DialerTheme.typography.headline.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1
            )
            if (name.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                androidx.compose.material3.Text(
                    text = number,
                    style = DialerTheme.typography.body.copy(color = colors.textSecondary)
                )
            }
            Spacer(Modifier.height(8.dp))
            androidx.compose.material3.Text(
                text = status,
                style = DialerTheme.typography.body.copy(color = colors.primary)
            )

            Spacer(Modifier.weight(1f))

            // Control bar (only meaningful while connected/dialing)
            if (callState !is CallState.Ringing) {
                val active = callState as? CallState.Active
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ControlButton(
                        icon = if (active?.isMuted == true) DialerIcons.MuteFilled else DialerIcons.Mute,
                        label = "Mute",
                        active = active?.isMuted == true,
                        onClick = viewModel::toggleMute
                    )
                    ControlButton(
                        icon = if (active?.isSpeakerOn == true) DialerIcons.SpeakerFilled else DialerIcons.Speaker,
                        label = "Speaker",
                        active = active?.isSpeakerOn == true,
                        onClick = viewModel::toggleSpeaker
                    )
                    ControlButton(
                        icon = DialerIcons.Record,
                        label = "Record",
                        active = active?.isRecording == true,
                        onClick = { /* recording toggle wired by recording feature */ }
                    )
                    ControlButton(
                        icon = DialerIcons.AddCall,
                        label = "Add",
                        active = false,
                        onClick = { /* add-call handled by telecom feature */ }
                    )
                }
                Spacer(Modifier.height(36.dp))
            }

            // Answer / End
            if (callState is CallState.Ringing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RoundActionButton(
                        icon = DialerIcons.EndCall,
                        background = colors.callRed,
                        contentDescription = "Decline",
                        onClick = viewModel::rejectCall
                    )
                    RoundActionButton(
                        icon = DialerIcons.PhoneFilled,
                        background = colors.callGreen,
                        contentDescription = "Answer",
                        onClick = viewModel::answerCall
                    )
                }
            } else {
                RoundActionButton(
                    icon = DialerIcons.EndCall,
                    background = colors.callRed,
                    contentDescription = "End call",
                    onClick = viewModel::endCall
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

/** Ticks once per second and returns the running call duration string. */
@Composable
private fun CallTimer(startTime: Long): String {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(startTime) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }
    val seconds = ((now - startTime).coerceAtLeast(0L)) / 1000
    return DateFormat.duration(seconds).ifEmpty { "0:00" }
}

@Composable
private fun PulsingAvatar(name: String, number: String, pulsing: Boolean) {
    val colors = DialerTheme.colors
    val transition = rememberInfiniteTransition(label = "callPulse")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = if (pulsing) 1.12f else 1f,
        animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Reverse),
        label = "pulseScale"
    )
    val ringAlpha by transition.animateFloat(
        initialValue = if (pulsing) 0.35f else 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Restart),
        label = "ringAlpha"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer { this.alpha = ringAlpha; scaleX = scale + 0.15f; scaleY = scale + 0.15f }
                .clip(CircleShape)
                .background(colors.primary.copy(alpha = 0.4f))
        )
        Box(modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }) {
            DialerAvatar(name = name, number = number, size = 128.dp)
        }
    }
}

@Composable
private fun ControlButton(
    icon: ImageVector,
    label: String,
    active: Boolean,
    onClick: () -> Unit
) {
    val colors = DialerTheme.colors
    val bg = if (active) colors.primary else colors.surface
    val tint = if (active) colors.onPrimary else colors.textPrimary
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(bg)
                .androidxClickable(onClick)
        ) {
            androidx.compose.foundation.Image(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(26.dp),
                colorFilter = ColorFilter.tint(tint)
            )
        }
        Spacer(Modifier.height(8.dp))
        androidx.compose.material3.Text(
            text = label,
            style = DialerTheme.typography.caption.copy(color = colors.textSecondary)
        )
    }
}

@Composable
private fun RoundActionButton(
    icon: ImageVector,
    background: Color,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(background)
            .androidxClickable(onClick)
    ) {
        androidx.compose.foundation.Image(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

/** Bounded-ripple clickable used by the circular in-call buttons. */
private fun Modifier.androidxClickable(onClick: () -> Unit): Modifier =
    androidx.compose.ui.composed {
        val interaction = remember {
            androidx.compose.foundation.interaction.MutableInteractionSource()
        }
        androidx.compose.foundation.clickable(
            interactionSource = interaction,
            indication = androidx.compose.material.ripple.rememberRipple(bounded = false),
            onClick = onClick
        )
    }
