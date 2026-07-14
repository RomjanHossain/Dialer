/**
 * DialerComponents.kt
 *
 * Reusable, theme-aware composable primitives for the Dialer app.
 * None of these depend on Material Design components beyond the most
 * basic [Surface] and [Text]. Every component reads its colours, shapes,
 * and dimensions from [DialerTheme] and [DesignTokens].
 */
package com.capx.dialer.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capx.dialer.core.ui.animation.AnimationEngine
import com.capx.dialer.core.ui.animation.pressAnimation
import com.capx.dialer.core.ui.animation.pulseTransition
import com.capx.dialer.core.ui.theme.DesignTokens
import com.capx.dialer.core.ui.theme.DialerTheme
import kotlin.math.PI
import kotlin.math.sin

// ─────────────────────────────────────────────────────────────────────────────
// 1. DialerSurface
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Rounded-corner card surface with a semi-transparent background and a
 * subtle tinted shadow.
 *
 * @param modifier       Modifier chain.
 * @param color          Background colour (defaults to [DialerTheme.colors.surface]).
 * @param elevation      Shadow depth (defaults to [DesignTokens.cardElevation]).
 * @param cornerRadius   Corner radius (defaults to [DesignTokens.cardCorner]).
 * @param content        Slot for child content.
 */
@Composable
fun DialerSurface(
    modifier: Modifier = Modifier,
    color: Color = DialerTheme.colors.surface,
    elevation: Dp = DesignTokens.cardElevation,
    cornerRadius: Dp = DesignTokens.cardCorner,
    content: @Composable () -> Unit
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = DialerTheme.colors.primary.copy(alpha = 0.08f),
                spotColor = DialerTheme.colors.primary.copy(alpha = 0.12f)
            )
            .clip(shape)
            .background(color)
    ) {
        content()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. DialerButton
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Pill-shaped button with a spring-loaded press animation.
 *
 * @param text           Button label.
 * @param onClick        Click callback.
 * @param modifier       Modifier chain.
 * @param enabled        Whether the button is interactive.
 * @param backgroundColor Button fill colour.
 * @param contentColor   Label / icon colour.
 */
@Composable
fun DialerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = DialerTheme.colors.primary,
    contentColor: Color = DialerTheme.colors.onPrimary
) {
    val alpha = if (enabled) 1f else DesignTokens.disabledContent

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .graphicsLayer { this.alpha = alpha }
            .pressAnimation(onPress = { if (enabled) onClick() })
            .clip(DialerTheme.shapes.pill)
            .background(backgroundColor)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        androidx.compose.material3.Text(
            text = text,
            style = DialerTheme.typography.body.copy(
                color = contentColor,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. DialerIconButton
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Circular icon button with a 44 dp minimum touch target.
 *
 * @param icon           [ImageVector] to display.
 * @param contentDescription Accessibility label.
 * @param onClick        Click callback.
 * @param modifier       Modifier chain.
 * @param tint           Icon tint colour.
 * @param backgroundColor Circle fill colour.
 * @param size           Overall button size.
 */
@Composable
fun DialerIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = DialerTheme.colors.onSurface,
    backgroundColor: Color = Color.Transparent,
    size: Dp = DesignTokens.touchTarget
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = androidx.compose.material.ripple.rememberRipple(
                    bounded = true,
                    radius = size / 2
                ),
                onClick = onClick
            )
    ) {
        androidx.compose.foundation.Image(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(DesignTokens.iconSize),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(tint)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. DialerBottomBar
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Data class representing a single tab in [DialerBottomBar].
 *
 * @param icon          Default (unselected) icon.
 * @param selectedIcon  Filled icon shown when this tab is active.
 * @param label         Tab label.
 */
data class DialerTab(
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val label: String,
    /** Optional badge count shown on the tab (0 = no badge). */
    val badgeCount: Int = 0
)

/**
 * Custom bottom tab bar with a sliding pill indicator that animates
 * between tabs using a spring animation.
 *
 * @param tabs          Ordered list of [DialerTab] items.
 * @param selectedIndex Currently selected tab index.
 * @param onTabSelected Callback with the newly-selected index.
 * @param modifier      Modifier chain.
 */
@Composable
fun DialerBottomBar(
    tabs: List<DialerTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = DialerTheme.colors
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(DesignTokens.tabBarHeight)
            .background(colors.surface)
    ) {
        // Divider at top
        Box(
            Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(colors.divider)
                .align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = index == selectedIndex

                val tint by animateColorAsState(
                    targetValue = if (isSelected) colors.primary else colors.onSurfaceVariant,
                    animationSpec = AnimationEngine.microTween(),
                    label = "tabTint"
                )
                val pillAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0f,
                    animationSpec = AnimationEngine.smallTween(),
                    label = "tabPillAlpha"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(index) }
                        )
                ) {
                    // Soft rounded "pill" highlight behind the selected icon so the
                    // active tab reads as a coloured icon, never a solid blob.
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(56.dp)
                            .height(30.dp)
                            .clip(DialerTheme.shapes.pill)
                            .background(colors.primary.copy(alpha = 0.14f * pillAlpha))
                    ) {
                        // Always use the outline icon and recolour it; this avoids the
                        // filled variants collapsing into an unrecognisable silhouette.
                        androidx.compose.foundation.Image(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier.size(DesignTokens.iconSize),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(tint)
                        )

                        if (tab.badgeCount > 0) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 10.dp, y = (-6).dp)
                                    .defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                                    .clip(CircleShape)
                                    .background(colors.callRed)
                                    .padding(horizontal = 5.dp)
                            ) {
                                androidx.compose.material3.Text(
                                    text = if (tab.badgeCount > 99) "99+" else tab.badgeCount.toString(),
                                    style = DialerTheme.typography.caption.copy(
                                        color = Color.White,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(3.dp))

                    androidx.compose.material3.Text(
                        text = tab.label,
                        style = DialerTheme.typography.caption.copy(
                            color = tint,
                            fontWeight = if (isSelected) {
                                androidx.compose.ui.text.font.FontWeight.SemiBold
                            } else {
                                androidx.compose.ui.text.font.FontWeight.Normal
                            }
                        )
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. DialerSearchBar
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Rounded search field with a leading search icon and a glassmorphic
 * translucent background.
 *
 * @param query          Current text value.
 * @param onQueryChange  Text-change callback.
 * @param modifier       Modifier chain.
 * @param placeholder    Placeholder/hint text.
 * @param searchIcon     Leading icon vector.
 */
@Composable
fun DialerSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    searchIcon: ImageVector? = null
) {
    val colors = DialerTheme.colors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(DialerTheme.shapes.pill)
            .background(colors.surfaceVariant.copy(alpha = DesignTokens.frostedGlass))
            .padding(horizontal = 12.dp)
    ) {
        // Leading icon
        if (searchIcon != null) {
            androidx.compose.foundation.Image(
                imageVector = searchIcon,
                contentDescription = "Search",
                modifier = Modifier.size(20.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(colors.textTertiary)
            )
            Spacer(Modifier.width(8.dp))
        }

        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                androidx.compose.material3.Text(
                    text = placeholder,
                    style = DialerTheme.typography.body.copy(color = colors.textTertiary)
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = DialerTheme.typography.body.copy(color = colors.textPrimary),
                cursorBrush = SolidColor(colors.primary),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 6. GlassmorphicCard
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Translucent card with rounded corners for a glass-like visual effect.
 * Uses alpha-blended surface colour (API-safe, no [Modifier.blur]).
 *
 * @param modifier   Modifier chain.
 * @param alpha      Background alpha (defaults to [DesignTokens.frostedGlass]).
 * @param content    Slot for child content.
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    alpha: Float = DesignTokens.frostedGlass,
    content: @Composable () -> Unit
) {
    val shape = DialerTheme.shapes.large
    Box(
        modifier = modifier
            .shadow(
                elevation = DesignTokens.cardElevation,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.06f)
            )
            .clip(shape)
            .background(DialerTheme.colors.surface.copy(alpha = alpha))
            .padding(16.dp)
    ) {
        content()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 7. DialerDivider
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Thin horizontal divider using the theme's divider colour and opacity.
 *
 * @param modifier Modifier chain.
 * @param startIndent Left inset for the divider line.
 */
@Composable
fun DialerDivider(
    modifier: Modifier = Modifier,
    startIndent: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = startIndent)
            .height(0.5.dp)
            .background(DialerTheme.colors.divider)
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// 8. WaveformView
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Simple waveform visualisation drawn on a [Canvas]. Each amplitude
 * value is rendered as a vertical bar. Suitable for recording or playback
 * indicators.
 *
 * @param amplitudes List of normalised amplitude values (0 f – 1 f).
 * @param modifier   Modifier chain.
 * @param barColor   Colour for the waveform bars.
 * @param barWidth   Width of each bar in dp.
 * @param barSpacing Horizontal gap between bars in dp.
 */
@Composable
fun WaveformView(
    amplitudes: List<Float>,
    modifier: Modifier = Modifier,
    barColor: Color = DialerTheme.colors.primary,
    barWidth: Dp = 3.dp,
    barSpacing: Dp = 2.dp
) {
    Canvas(modifier = modifier) {
        val canvasHeight = size.height
        val canvasWidth = size.width
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()
        val totalBarWidth = barWidthPx + barSpacingPx
        val maxBars = (canvasWidth / totalBarWidth).toInt()
        val cornerRadiusPx = barWidthPx / 2

        // Only draw the most recent bars that fit
        val visibleAmps = if (amplitudes.size > maxBars) {
            amplitudes.takeLast(maxBars)
        } else {
            amplitudes
        }

        visibleAmps.forEachIndexed { index, amp ->
            val clampedAmp = amp.coerceIn(0.05f, 1f)
            val barHeight = canvasHeight * clampedAmp
            val x = index * totalBarWidth
            val y = (canvasHeight - barHeight) / 2

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 9. PulsingDot
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Red pulsing dot used as a recording indicator. Uses
 * [pulseTransition] to continuously animate scale and opacity.
 *
 * @param modifier Modifier chain.
 * @param color    Dot colour (defaults to [DialerTheme.colors.recordingRed]).
 * @param size     Base size of the dot.
 */
@Composable
fun PulsingDot(
    modifier: Modifier = Modifier,
    color: Color = DialerTheme.colors.recordingRed,
    size: Dp = 10.dp
) {
    val pulse = pulseTransition()

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = pulse.scale.value
                scaleY = pulse.scale.value
                alpha = pulse.alpha.value
            }
            .clip(CircleShape)
            .background(color)
    )
}
