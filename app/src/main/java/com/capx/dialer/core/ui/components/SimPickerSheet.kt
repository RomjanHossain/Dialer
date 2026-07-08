package com.capx.dialer.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.capx.dialer.core.domain.model.SimAccount
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DesignTokens
import com.capx.dialer.core.ui.theme.DialerTheme

/**
 * Custom glassmorphic bottom sheet that lets the user pick which SIM to call
 * with. Appears instantly (accounts are pre-loaded) so it replaces the slow
 * system SIM chooser. Rendered as a full-screen overlay so it can sit above
 * any screen.
 *
 * @param visible Whether the sheet is shown.
 * @param sims Accounts to choose from.
 * @param onSelect Invoked with the chosen [SimAccount].
 * @param onDismiss Invoked when the scrim is tapped.
 */
@Composable
fun SimPickerSheet(
    visible: Boolean,
    sims: List<SimAccount>,
    onSelect: (SimAccount) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = DialerTheme.colors

    Box(modifier = modifier.fillMaxSize()) {
        // Scrim
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(250)),
            exit = fadeOut(tween(250))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = DesignTokens.overlay))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    )
            )
        }

        // Sheet
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(tween(300)) { it } + fadeIn(tween(250)),
            exit = slideOutVertically(tween(220)) { it } + fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(DialerTheme.shapes.sheet)
                    .background(colors.surface)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(DesignTokens.sheetDragHandleWidth)
                        .height(DesignTokens.sheetDragHandleHeight)
                        .clip(CircleShape)
                        .background(colors.divider)
                )
                Spacer(Modifier.height(16.dp))

                androidx.compose.material3.Text(
                    text = "Call with",
                    style = DialerTheme.typography.title.copy(
                        color = colors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.height(12.dp))

                sims.forEachIndexed { index, sim ->
                    SimRow(
                        sim = sim,
                        accent = if (index % 2 == 0) colors.primary else colors.secondary,
                        onClick = { onSelect(sim) }
                    )
                    if (index != sims.lastIndex) Spacer(Modifier.height(4.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SimRow(
    sim: SimAccount,
    accent: Color,
    onClick: () -> Unit
) {
    val colors = DialerTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(DialerTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(accent.copy(alpha = 0.15f))
        ) {
            androidx.compose.foundation.Image(
                imageVector = DialerIcons.Phone,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(accent)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            androidx.compose.material3.Text(
                text = sim.label,
                style = DialerTheme.typography.body.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Medium
                )
            )
            if (sim.slotIndex >= 0) {
                androidx.compose.material3.Text(
                    text = "SIM slot ${sim.slotIndex + 1}",
                    style = DialerTheme.typography.caption.copy(color = colors.textSecondary)
                )
            }
        }
    }
}
