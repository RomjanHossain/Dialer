/**
 * DesignTokens.kt
 *
 * Single source of truth for all visual constants used throughout the Dialer
 * app. Every composable should reference values from this object instead of
 * hard-coding magic numbers, ensuring consistency and easy global tuning.
 *
 * Categories:
 * - **Dimensions** — sizes, touch targets, corner radii
 * - **Opacities** — alpha values for glass effects, overlays, disabled states
 * - **Elevations** — shadow depth tokens
 * - **AnimationDurations** — timing presets (in milliseconds)
 */
package com.capx.dialer.core.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object DesignTokens {

    // ── Dimensions ──────────────────────────────────────────────────────────

    /** Default icon size used across the app. */
    val iconSize: Dp = 24.dp

    /** Minimum recommended touch target (accessibility). */
    val touchTarget: Dp = 44.dp

    /** Height of the bottom tab bar. */
    val tabBarHeight: Dp = 64.dp

    /** Height of a single dial-pad button. */
    val dialpadButtonHeight: Dp = 48.dp

    /** Width of a single dial-pad button. */
    val dialpadButtonWidth: Dp = 72.dp

    /** Top-corner radius for bottom sheets. */
    val bottomSheetCorner: Dp = 24.dp

    /** Default card corner radius. */
    val cardCorner: Dp = 16.dp

    /** Height of the bottom-sheet drag handle. */
    val sheetDragHandleHeight: Dp = 4.dp

    /** Width of the bottom-sheet drag handle. */
    val sheetDragHandleWidth: Dp = 36.dp

    // ── Opacities ───────────────────────────────────────────────────────────

    /** Alpha for frosted-glass / glassmorphic surfaces. */
    const val frostedGlass: Float = 0.7f

    /** Alpha for scrim overlays behind dialogs & sheets. */
    const val overlay: Float = 0.5f

    /** Alpha for disabled UI content. */
    const val disabledContent: Float = 0.38f

    /** Alpha for thin dividers. */
    const val divider: Float = 0.12f

    // ── Elevations ──────────────────────────────────────────────────────────

    /** Subtle card shadow. */
    val cardElevation: Dp = 2.dp

    /** Raised element shadow (FABs, elevated buttons). */
    val raisedElevation: Dp = 4.dp

    /** Bottom-sheet shadow. */
    val sheetElevation: Dp = 8.dp

    /** Dialog / modal shadow. */
    val dialogElevation: Dp = 16.dp

    // ── Animation Durations (ms) ────────────────────────────────────────────

    /** Ultra-short transition — icon morphs, color fades. */
    const val durationMicro: Int = 150

    /** Short transition — small element enter/exit. */
    const val durationSmall: Int = 250

    /** Standard transition — screen elements. */
    const val durationMedium: Int = 350

    /** Long transition — full-screen or complex choreography. */
    const val durationLarge: Int = 500
}
