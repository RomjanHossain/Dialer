/**
 * AnimationEngine.kt
 *
 * Centralised animation toolkit for the Dialer app. Provides pre-configured
 * spring and tween specs, plus reusable helpers for common motion patterns
 * such as recording-indicator pulsing and spring-loaded button presses.
 *
 * All composables should use these specs instead of creating ad-hoc
 * animation configurations.
 */
package com.capx.dialer.core.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.capx.dialer.core.ui.theme.DesignTokens
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// Spring Specs
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Central object that holds every reusable animation spec in the design system.
 */
object AnimationEngine {

    // ── Springs ─────────────────────────────────────────────────────────────

    /**
     * Default spring used for most interactive transitions.
     * Slightly under-damped for a playful bounce.
     */
    fun <T> defaultSpring(): AnimationSpec<T> = spring(
        dampingRatio = 0.6f,
        stiffness = 400f
    )

    /**
     * Gentle spring for subtle, non-urgent motions (fades, sheet reveals).
     */
    fun <T> gentleSpring(): AnimationSpec<T> = spring(
        dampingRatio = 0.8f,
        stiffness = 200f
    )

    /**
     * Snappy spring for instant-feedback interactions (tab switches, toggles).
     */
    fun <T> snappySpring(): AnimationSpec<T> = spring(
        dampingRatio = 0.5f,
        stiffness = 800f
    )

    // ── Tweens ──────────────────────────────────────────────────────────────

    /** Ultra-short tween — icon morphs, small color fades. */
    fun <T> microTween(): AnimationSpec<T> = tween(
        durationMillis = DesignTokens.durationMicro,
        easing = FastOutSlowInEasing
    )

    /** Short tween — small element enter/exit. */
    fun <T> smallTween(): AnimationSpec<T> = tween(
        durationMillis = DesignTokens.durationSmall,
        easing = FastOutSlowInEasing
    )

    /** Standard tween — screen-level transitions. */
    fun <T> mediumTween(): AnimationSpec<T> = tween(
        durationMillis = DesignTokens.durationMedium,
        easing = FastOutSlowInEasing
    )

    /** Long tween — complex choreographed sequences. */
    fun <T> largeTween(): AnimationSpec<T> = tween(
        durationMillis = DesignTokens.durationLarge,
        easing = FastOutSlowInEasing
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Pulse Transition (recording indicator)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Data class holding the animated scale and alpha values for a pulsing
 * recording indicator.
 */
data class PulseValues(
    val scale: State<Float>,
    val alpha: State<Float>
)

/**
 * Creates an [InfiniteTransition]-based pulse effect suitable for
 * recording indicators (red dot, waveform glow, etc.).
 *
 * @return [PulseValues] with animated `scale` (1 f → 1.3 f) and
 *         `alpha` (1 f → 0.4 f) states.
 */
@Composable
fun pulseTransition(): PulseValues {
    val transition = rememberInfiniteTransition(label = "pulse")

    val scale = transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val alpha = transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    return PulseValues(scale = scale, alpha = alpha)
}

// ─────────────────────────────────────────────────────────────────────────────
// Press Animation (Modifier extension)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Adds a spring-loaded press effect to any composable. On press-down the
 * element scales to [pressedScale]; on release it springs back to 1 f.
 *
 * The scale animations run in a separate coroutine so they never block the
 * gesture, and [onPress] fires on tap (release) immediately — this keeps rapid
 * dial-pad typing responsive instead of throttling to the animation duration.
 *
 * @param pressedScale Scale multiplier while pressed (default 0.92 f).
 * @param onPress      Callback invoked on successful tap.
 */
fun Modifier.pressAnimation(
    pressedScale: Float = 0.92f,
    onPress: () -> Unit = {}
): Modifier = composed {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    this
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    scope.launch { scale.animateTo(pressedScale, AnimationEngine.snappySpring()) }
                    tryAwaitRelease()
                    scope.launch { scale.animateTo(1f, AnimationEngine.defaultSpring()) }
                },
                onTap = { onPress() }
            )
        }
}
