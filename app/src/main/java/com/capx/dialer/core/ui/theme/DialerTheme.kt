/**
 * DialerTheme.kt
 *
 * Custom design system theme for the Dialer app. Provides [DialerColorScheme],
 * [DialerTypography], and [DialerShape] via [CompositionLocal] — completely
 * independent of Material Theme. All screen-level composables should be wrapped
 * in [DialerTheme] to gain access to the design tokens.
 *
 * Usage:
 * ```
 * DialerTheme(darkTheme = isSystemInDarkTheme()) {
 *     // Access tokens via DialerTheme.colors, DialerTheme.typography, DialerTheme.shapes
 *     Text(
 *         text = "Hello",
 *         color = DialerTheme.colors.textPrimary,
 *         style = DialerTheme.typography.body
 *     )
 * }
 * ```
 */
package com.capx.dialer.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────────────────────
// Color Scheme
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Complete color palette for the Dialer design system.
 *
 * Two pre-built palettes are available: [LightDialerColors] (cool-white / ice base)
 * and [DarkDialerColors] (true-black OLED). Custom palettes can be created for
 * dynamic-color or accessibility modes.
 */
@Immutable
data class DialerColorScheme(
    /** Primary app background. */
    val background: Color,
    /** Card / elevated surface. */
    val surface: Color,
    /** Secondary surface for grouped content or input fields. */
    val surfaceVariant: Color,
    /** Brand accent — used for active tabs, CTAs, FABs. */
    val primary: Color,
    /** Foreground drawn on top of [primary]. */
    val onPrimary: Color,
    /** Secondary accent for less prominent elements. */
    val secondary: Color,
    /** Foreground drawn on top of [secondary]. */
    val onSecondary: Color,
    /** Primary foreground on [surface]. */
    val onSurface: Color,
    /** De-emphasised foreground on [surfaceVariant]. */
    val onSurfaceVariant: Color,
    /** Error / destructive states. */
    val error: Color,
    /** Tertiary accent for highlights, badges, etc. */
    val accent: Color,
    /** Deep crimson for recording indicator. */
    val recordingRed: Color,
    /** Thin line dividers. */
    val divider: Color,
    /** "Answer" / active-call green. */
    val callGreen: Color,
    /** "Decline" / end-call red. */
    val callRed: Color,
    /** Missed-call indicator. */
    val missedCall: Color,
    /** Highest-contrast text. */
    val textPrimary: Color,
    /** Medium-emphasis text. */
    val textSecondary: Color,
    /** Lowest-emphasis / hint text. */
    val textTertiary: Color
)

/** Cool-white palette — ice-blue undertone, deep-teal accent. */
val LightDialerColors = DialerColorScheme(
    background       = Color(0xFFF2F2F7),
    surface           = Color(0xFFFFFFFF),
    surfaceVariant    = Color(0xFFE5E5EA),
    primary           = Color(0xFF0A7E8C),  // deep teal
    onPrimary         = Color(0xFFFFFFFF),
    secondary         = Color(0xFF5856D6),  // indigo accent
    onSecondary       = Color(0xFFFFFFFF),
    onSurface         = Color(0xFF1C1C1E),
    onSurfaceVariant  = Color(0xFF636366),
    error             = Color(0xFFD32F2F),
    accent            = Color(0xFF30B0C7),  // luminous teal-cyan
    recordingRed      = Color(0xFFD32F2F),
    divider           = Color(0x1F000000),  // 12 % black
    callGreen         = Color(0xFF34C759),
    callRed           = Color(0xFFFF3B30),
    missedCall        = Color(0xFFFF9500),
    textPrimary       = Color(0xFF000000),
    textSecondary     = Color(0xFF636366),
    textTertiary      = Color(0xFFAEAEB2)
)

/** True-black OLED palette with luminous teal accent. */
val DarkDialerColors = DialerColorScheme(
    background       = Color(0xFF000000),
    surface           = Color(0xFF1C1C1E),
    surfaceVariant    = Color(0xFF2C2C2E),
    primary           = Color(0xFF64D2FF),  // luminous teal
    onPrimary         = Color(0xFF003544),
    secondary         = Color(0xFFBF5AF2),  // vivid purple
    onSecondary       = Color(0xFF1A0033),
    onSurface         = Color(0xFFFFFFFF),
    onSurfaceVariant  = Color(0xFFAEAEB2),
    error             = Color(0xFFFF6B6B),
    accent            = Color(0xFF5AC8FA),  // luminous blue-cyan
    recordingRed      = Color(0xFFE53935),
    divider           = Color(0x1FFFFFFF),  // 12 % white
    callGreen         = Color(0xFF30D158),
    callRed           = Color(0xFFFF453A),
    missedCall        = Color(0xFFFF9F0A),
    textPrimary       = Color(0xFFFFFFFF),
    textSecondary     = Color(0xFFAEAEB2),
    textTertiary      = Color(0xFF636366)
)

// ─────────────────────────────────────────────────────────────────────────────
// Typography
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Type scale for the Dialer design system. Uses the platform system font
 * (no custom typefaces) with a 9-step scale from 12 sp to 40 sp.
 */
@Immutable
data class DialerTypography(
    /** 12 sp — timestamps, badges. */
    val caption: TextStyle,
    /** 14 sp — secondary body text. */
    val bodySmall: TextStyle,
    /** 16 sp — primary body text. */
    val body: TextStyle,
    /** 18 sp — list-item titles. */
    val titleSmall: TextStyle,
    /** 20 sp — section titles. */
    val title: TextStyle,
    /** 24 sp — small headlines. */
    val headlineSmall: TextStyle,
    /** 28 sp — headlines. */
    val headline: TextStyle,
    /** 32 sp — small display / dial-pad digits. */
    val displaySmall: TextStyle,
    /** 40 sp — hero display text. */
    val display: TextStyle
)

/** Default typography instance using system font. */
val DefaultDialerTypography = DialerTypography(
    caption = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.4.sp
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp
    ),
    body = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp
    ),
    title = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp
    ),
    headline = TextStyle(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.25).sp
    ),
    display = TextStyle(
        fontSize = 40.sp,
        lineHeight = 48.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp
    )
)

// ─────────────────────────────────────────────────────────────────────────────
// Shape System
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Corner-radius tokens for the Dialer design system.
 */
@Immutable
data class DialerShape(
    /** 8 dp — small chips, badges. */
    val small: RoundedCornerShape,
    /** 12 dp — cards, buttons. */
    val medium: RoundedCornerShape,
    /** 16 dp — large cards, dialogs. */
    val large: RoundedCornerShape,
    /** 20 dp — modals, prominent surfaces. */
    val extraLarge: RoundedCornerShape,
    /** 50 % — pill buttons, search bars. */
    val pill: RoundedCornerShape,
    /** 24 dp top corners — bottom sheets. */
    val sheet: RoundedCornerShape
)

/** Default shape set. */
val DefaultDialerShape = DialerShape(
    small      = RoundedCornerShape(8.dp),
    medium     = RoundedCornerShape(12.dp),
    large      = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp),
    pill       = RoundedCornerShape(50),
    sheet      = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
)

// ─────────────────────────────────────────────────────────────────────────────
// CompositionLocals
// ─────────────────────────────────────────────────────────────────────────────

/** Composition-local for the current [DialerColorScheme]. */
val LocalDialerColors = staticCompositionLocalOf { LightDialerColors }

/** Composition-local for the current [DialerTypography]. */
val LocalDialerTypography = staticCompositionLocalOf { DefaultDialerTypography }

/** Composition-local for the current [DialerShape]. */
val LocalDialerShapes = staticCompositionLocalOf { DefaultDialerShape }

// ─────────────────────────────────────────────────────────────────────────────
// Theme Composable & Accessor
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Root theme composable for every screen in the Dialer app.
 *
 * Provides [DialerColorScheme], [DialerTypography], and [DialerShape]
 * through [CompositionLocal] so that any descendant composable can
 * access design tokens via [DialerTheme.colors], [DialerTheme.typography],
 * and [DialerTheme.shapes].
 *
 * @param darkTheme Whether to use the dark color palette. Defaults to system setting.
 * @param content    Slot for the themed content tree.
 */
@Composable
fun DialerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkDialerColors else LightDialerColors

    CompositionLocalProvider(
        LocalDialerColors provides colors,
        LocalDialerTypography provides DefaultDialerTypography,
        LocalDialerShapes provides DefaultDialerShape,
        content = content
    )
}

/**
 * Singleton accessor for the current Dialer design-system tokens.
 *
 * Must be called from within a [DialerTheme] composable scope.
 *
 * ```kotlin
 * val bg = DialerTheme.colors.background
 * val body = DialerTheme.typography.body
 * val pill = DialerTheme.shapes.pill
 * ```
 */
object DialerTheme {
    /** Current [DialerColorScheme] provided by [DialerTheme]. */
    val colors: DialerColorScheme
        @Composable get() = LocalDialerColors.current

    /** Current [DialerTypography] provided by [DialerTheme]. */
    val typography: DialerTypography
        @Composable get() = LocalDialerTypography.current

    /** Current [DialerShape] provided by [DialerTheme]. */
    val shapes: DialerShape
        @Composable get() = LocalDialerShapes.current
}
