/**
 * DialerIcons.kt
 *
 * Custom stroke-based icon set for the Dialer app. Every icon is built with
 * [ImageVector.Builder] using thin stroke paths (1.5–2 dp stroke width) for
 * a modern, lightweight aesthetic.
 *
 * All icons default to 24 × 24 dp with a 24 × 24 viewport. Use these via
 * `DialerIcons.Phone`, `DialerIcons.Contacts`, etc.
 */
package com.capx.dialer.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Singleton containing all custom [ImageVector] icons used in the Dialer app.
 *
 * Naming convention:
 * - Outline/stroke variant: `Phone`, `Contacts`, `Clock` …
 * - Filled variant: `PhoneFilled`, `ContactsFilled`, `ClockFilled` …
 */
object DialerIcons {

    // ── Shared defaults ─────────────────────────────────────────────────────

    private val defaultSize = 24.dp
    private val viewportSize = 24f
    private val strokeColor = SolidColor(Color.Black)
    private val fillColor = SolidColor(Color.Black)
    private const val defaultStrokeWidth = 1.8f

    // ── Phone ───────────────────────────────────────────────────────────────

    val Phone: ImageVector by lazy {
        ImageVector.Builder(
            name = "Phone",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Phone handset shape
                moveTo(22f, 16.92f)
                lineTo(22f, 19.92f)
                curveTo(22f, 20.48f, 21.47f, 20.95f, 20.92f, 20.97f)
                curveTo(18.56f, 21.19f, 16.28f, 20.73f, 14.22f, 19.75f)
                curveTo(12.3f, 18.88f, 10.59f, 17.56f, 9.21f, 15.93f)
                curveTo(7.58f, 14.55f, 6.26f, 12.84f, 5.39f, 10.92f)
                curveTo(4.41f, 8.84f, 3.95f, 6.54f, 4.17f, 4.18f)
                curveTo(4.19f, 3.63f, 4.66f, 3.1f, 5.22f, 3.1f)
                lineTo(8.22f, 3.1f)
                curveTo(8.69f, 3.06f, 9.11f, 3.39f, 9.2f, 3.86f)
                curveTo(9.38f, 5.02f, 9.71f, 6.15f, 10.18f, 7.22f)
                curveTo(10.34f, 7.58f, 10.25f, 8f, 9.96f, 8.29f)
                lineTo(8.69f, 9.57f)
                curveTo(9.97f, 11.77f, 11.73f, 13.53f, 13.93f, 14.81f)
                lineTo(15.21f, 13.54f)
                curveTo(15.5f, 13.25f, 15.92f, 13.16f, 16.28f, 13.32f)
                curveTo(17.35f, 13.79f, 18.48f, 14.12f, 19.64f, 14.3f)
                curveTo(20.11f, 14.39f, 20.44f, 14.81f, 20.4f, 15.28f)
                close()
            }
        }.build()
    }

    val PhoneFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "PhoneFilled",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                fill = fillColor,
                strokeLineWidth = 0f
            ) {
                moveTo(22f, 16.92f)
                lineTo(22f, 19.92f)
                curveTo(22f, 20.48f, 21.47f, 20.95f, 20.92f, 20.97f)
                curveTo(18.56f, 21.19f, 16.28f, 20.73f, 14.22f, 19.75f)
                curveTo(12.3f, 18.88f, 10.59f, 17.56f, 9.21f, 15.93f)
                curveTo(7.58f, 14.55f, 6.26f, 12.84f, 5.39f, 10.92f)
                curveTo(4.41f, 8.84f, 3.95f, 6.54f, 4.17f, 4.18f)
                curveTo(4.19f, 3.63f, 4.66f, 3.1f, 5.22f, 3.1f)
                lineTo(8.22f, 3.1f)
                curveTo(8.69f, 3.06f, 9.11f, 3.39f, 9.2f, 3.86f)
                curveTo(9.38f, 5.02f, 9.71f, 6.15f, 10.18f, 7.22f)
                curveTo(10.34f, 7.58f, 10.25f, 8f, 9.96f, 8.29f)
                lineTo(8.69f, 9.57f)
                curveTo(9.97f, 11.77f, 11.73f, 13.53f, 13.93f, 14.81f)
                lineTo(15.21f, 13.54f)
                curveTo(15.5f, 13.25f, 15.92f, 13.16f, 16.28f, 13.32f)
                curveTo(17.35f, 13.79f, 18.48f, 14.12f, 19.64f, 14.3f)
                curveTo(20.11f, 14.39f, 20.44f, 14.81f, 20.4f, 15.28f)
                close()
            }
        }.build()
    }

    // ── Contacts ────────────────────────────────────────────────────────────

    val Contacts: ImageVector by lazy {
        ImageVector.Builder(
            name = "Contacts",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Head circle
                moveTo(16f, 8f)
                curveTo(16f, 10.21f, 14.21f, 12f, 12f, 12f)
                curveTo(9.79f, 12f, 8f, 10.21f, 8f, 8f)
                curveTo(8f, 5.79f, 9.79f, 4f, 12f, 4f)
                curveTo(14.21f, 4f, 16f, 5.79f, 16f, 8f)
                close()
                // Body arc
                moveTo(20f, 21f)
                curveTo(20f, 16.58f, 16.42f, 13f, 12f, 13f)
                curveTo(7.58f, 13f, 4f, 16.58f, 4f, 21f)
            }
        }.build()
    }

    val ContactsFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "ContactsFilled",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(fill = fillColor) {
                // Filled head
                moveTo(12f, 12f)
                curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f)
                curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f)
                curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f)
                curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f)
                close()
            }
            path(fill = fillColor) {
                // Filled body
                moveTo(12f, 14f)
                curveTo(7.58f, 14f, 4f, 17.13f, 4f, 21f)
                lineTo(20f, 21f)
                curveTo(20f, 17.13f, 16.42f, 14f, 12f, 14f)
                close()
            }
        }.build()
    }

    // ── Clock ───────────────────────────────────────────────────────────────

    val Clock: ImageVector by lazy {
        ImageVector.Builder(
            name = "Clock",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Circle
                addOval(4f, 4f, 16f)
                // Hour hand
                moveTo(12f, 7f)
                lineTo(12f, 12f)
                // Minute hand
                lineTo(15.5f, 14f)
            }
        }.build()
    }

    val ClockFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "ClockFilled",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(fill = fillColor) {
                addOval(2f, 2f, 20f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 7f)
                lineTo(12f, 12f)
                lineTo(15.5f, 14f)
            }
        }.build()
    }

    // ── Mic ─────────────────────────────────────────────────────────────────

    val Mic: ImageVector by lazy {
        ImageVector.Builder(
            name = "Mic",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Mic body
                moveTo(12f, 2f)
                curveTo(10.34f, 2f, 9f, 3.34f, 9f, 5f)
                lineTo(9f, 12f)
                curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
                curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
                lineTo(15f, 5f)
                curveTo(15f, 3.34f, 13.66f, 2f, 12f, 2f)
                close()
                // Pickup arc
                moveTo(19f, 10f)
                curveTo(19f, 13.87f, 15.87f, 17f, 12f, 17f)
                curveTo(8.13f, 17f, 5f, 13.87f, 5f, 10f)
                // Stand
                moveTo(12f, 17f)
                lineTo(12f, 22f)
                // Base
                moveTo(8f, 22f)
                lineTo(16f, 22f)
            }
        }.build()
    }

    val MicFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "MicFilled",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(fill = fillColor) {
                moveTo(12f, 2f)
                curveTo(10.34f, 2f, 9f, 3.34f, 9f, 5f)
                lineTo(9f, 12f)
                curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
                curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
                lineTo(15f, 5f)
                curveTo(15f, 3.34f, 13.66f, 2f, 12f, 2f)
                close()
            }
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(19f, 10f)
                curveTo(19f, 13.87f, 15.87f, 17f, 12f, 17f)
                curveTo(8.13f, 17f, 5f, 13.87f, 5f, 10f)
                moveTo(12f, 17f)
                lineTo(12f, 22f)
                moveTo(8f, 22f)
                lineTo(16f, 22f)
            }
        }.build()
    }

    // ── Keypad ──────────────────────────────────────────────────────────────

    val Keypad: ImageVector by lazy {
        ImageVector.Builder(
            name = "Keypad",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            // 3x4 grid of small circles representing dial-pad buttons
            val positions = listOf(
                6f to 4f, 12f to 4f, 18f to 4f,
                6f to 9f, 12f to 9f, 18f to 9f,
                6f to 14f, 12f to 14f, 18f to 14f,
                12f to 19f
            )
            for ((cx, cy) in positions) {
                path(
                    stroke = strokeColor,
                    strokeLineWidth = defaultStrokeWidth
                ) {
                    addOval(cx - 1.5f, cy - 1.5f, 3f)
                }
            }
        }.build()
    }

    // ── Speaker ─────────────────────────────────────────────────────────────

    val Speaker: ImageVector by lazy {
        ImageVector.Builder(
            name = "Speaker",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Speaker body
                moveTo(11f, 5f)
                lineTo(6f, 9f)
                lineTo(2f, 9f)
                lineTo(2f, 15f)
                lineTo(6f, 15f)
                lineTo(11f, 19f)
                close()
                // Sound wave 1
                moveTo(15.54f, 8.46f)
                curveTo(16.48f, 9.4f, 17.04f, 10.67f, 17.04f, 12f)
                curveTo(17.04f, 13.33f, 16.48f, 14.6f, 15.54f, 15.54f)
                // Sound wave 2
                moveTo(18.07f, 5.93f)
                curveTo(19.95f, 7.81f, 21.04f, 9.85f, 21.04f, 12f)
                curveTo(21.04f, 14.15f, 19.95f, 16.19f, 18.07f, 18.07f)
            }
        }.build()
    }

    val SpeakerFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "SpeakerFilled",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(fill = fillColor) {
                moveTo(11f, 5f)
                lineTo(6f, 9f)
                lineTo(2f, 9f)
                lineTo(2f, 15f)
                lineTo(6f, 15f)
                lineTo(11f, 19f)
                close()
            }
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15.54f, 8.46f)
                curveTo(16.48f, 9.4f, 17.04f, 10.67f, 17.04f, 12f)
                curveTo(17.04f, 13.33f, 16.48f, 14.6f, 15.54f, 15.54f)
                moveTo(18.07f, 5.93f)
                curveTo(19.95f, 7.81f, 21.04f, 9.85f, 21.04f, 12f)
                curveTo(21.04f, 14.15f, 19.95f, 16.19f, 18.07f, 18.07f)
            }
        }.build()
    }

    // ── Mute ────────────────────────────────────────────────────────────────

    val Mute: ImageVector by lazy {
        ImageVector.Builder(
            name = "Mute",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Mic body
                moveTo(12f, 2f)
                curveTo(10.34f, 2f, 9f, 3.34f, 9f, 5f)
                lineTo(9f, 12f)
                curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
                curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
                lineTo(15f, 5f)
                curveTo(15f, 3.34f, 13.66f, 2f, 12f, 2f)
                close()
                // Strike-through line
                moveTo(3f, 3f)
                lineTo(21f, 21f)
            }
        }.build()
    }

    val MuteFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "MuteFilled",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(fill = fillColor) {
                moveTo(12f, 2f)
                curveTo(10.34f, 2f, 9f, 3.34f, 9f, 5f)
                lineTo(9f, 12f)
                curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
                curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
                lineTo(15f, 5f)
                curveTo(15f, 3.34f, 13.66f, 2f, 12f, 2f)
                close()
            }
            path(
                stroke = strokeColor,
                strokeLineWidth = 2.2f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(3f, 3f)
                lineTo(21f, 21f)
            }
        }.build()
    }

    // ── AddCall ─────────────────────────────────────────────────────────────

    val AddCall: ImageVector by lazy {
        ImageVector.Builder(
            name = "AddCall",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Plus sign
                moveTo(17f, 2f)
                lineTo(17f, 10f)
                moveTo(13f, 6f)
                lineTo(21f, 6f)
                // Small phone
                moveTo(20f, 16.92f)
                lineTo(20f, 18.92f)
                curveTo(20f, 19.48f, 19.5f, 19.95f, 18.96f, 19.97f)
                curveTo(17.07f, 20.14f, 15.23f, 19.77f, 13.56f, 19f)
                curveTo(12f, 18.3f, 10.61f, 17.22f, 9.44f, 15.91f)
                curveTo(8.13f, 14.74f, 7.05f, 13.35f, 6.35f, 11.79f)
                curveTo(5.58f, 10.1f, 5.21f, 8.24f, 5.38f, 6.35f)
                curveTo(5.4f, 5.81f, 5.87f, 5.34f, 6.42f, 5.34f)
                lineTo(8.42f, 5.34f)
            }
        }.build()
    }

    // ── EndCall ─────────────────────────────────────────────────────────────

    val EndCall: ImageVector by lazy {
        ImageVector.Builder(
            name = "EndCall",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Horizontal handset hanging up
                moveTo(2f, 12f)
                curveTo(2f, 9.5f, 6.48f, 7f, 12f, 7f)
                curveTo(17.52f, 7f, 22f, 9.5f, 22f, 12f)
                lineTo(19.5f, 14f)
                lineTo(17f, 12f)
                curveTo(17f, 11f, 14.76f, 10f, 12f, 10f)
                curveTo(9.24f, 10f, 7f, 11f, 7f, 12f)
                lineTo(4.5f, 14f)
                close()
            }
        }.build()
    }

    // ── Delete ──────────────────────────────────────────────────────────────

    val Delete: ImageVector by lazy {
        ImageVector.Builder(
            name = "Delete",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Trash can
                moveTo(3f, 6f)
                lineTo(21f, 6f)
                moveTo(8f, 6f)
                lineTo(8f, 4f)
                curveTo(8f, 3.45f, 8.45f, 3f, 9f, 3f)
                lineTo(15f, 3f)
                curveTo(15.55f, 3f, 16f, 3.45f, 16f, 4f)
                lineTo(16f, 6f)
                moveTo(19f, 6f)
                lineTo(19f, 20f)
                curveTo(19f, 20.55f, 18.55f, 21f, 18f, 21f)
                lineTo(6f, 21f)
                curveTo(5.45f, 21f, 5f, 20.55f, 5f, 20f)
                lineTo(5f, 6f)
                // Inner lines
                moveTo(10f, 11f)
                lineTo(10f, 17f)
                moveTo(14f, 11f)
                lineTo(14f, 17f)
            }
        }.build()
    }

    // ── Backspace ───────────────────────────────────────────────────────────

    val Backspace: ImageVector by lazy {
        ImageVector.Builder(
            name = "Backspace",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Backspace shape
                moveTo(21f, 4f)
                lineTo(8f, 4f)
                lineTo(2f, 12f)
                lineTo(8f, 20f)
                lineTo(21f, 20f)
                curveTo(21.55f, 20f, 22f, 19.55f, 22f, 19f)
                lineTo(22f, 5f)
                curveTo(22f, 4.45f, 21.55f, 4f, 21f, 4f)
                close()
                // X inside
                moveTo(17f, 9f)
                lineTo(11f, 15f)
                moveTo(11f, 9f)
                lineTo(17f, 15f)
            }
        }.build()
    }

    // ── Search ──────────────────────────────────────────────────────────────

    val Search: ImageVector by lazy {
        ImageVector.Builder(
            name = "Search",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Magnifying glass circle
                addOval(3f, 3f, 14f)
                // Handle
                moveTo(16f, 16f)
                lineTo(21f, 21f)
            }
        }.build()
    }

    // ── Message ─────────────────────────────────────────────────────────────

    val Message: ImageVector by lazy {
        ImageVector.Builder(
            name = "Message",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Rounded speech bubble with a tail
                moveTo(21f, 15f)
                curveTo(21f, 16.1f, 20.1f, 17f, 19f, 17f)
                lineTo(8f, 17f)
                lineTo(4f, 21f)
                lineTo(4f, 6f)
                curveTo(4f, 4.9f, 4.9f, 4f, 6f, 4f)
                lineTo(19f, 4f)
                curveTo(20.1f, 4f, 21f, 4.9f, 21f, 6f)
                close()
            }
        }.build()
    }

    // ── Voicemail ───────────────────────────────────────────────────────────

    val Voicemail: ImageVector by lazy {
        ImageVector.Builder(
            name = "Voicemail",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Left circle
                addOval(1f, 7f, 10f)
                // Right circle
                addOval(13f, 7f, 10f)
                // Bottom connecting line
                moveTo(6f, 17f)
                lineTo(18f, 17f)
            }
        }.build()
    }

    // ── Star ────────────────────────────────────────────────────────────────

    val Star: ImageVector by lazy {
        ImageVector.Builder(
            name = "Star",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 2f)
                lineTo(15.09f, 8.26f)
                lineTo(22f, 9.27f)
                lineTo(17f, 14.14f)
                lineTo(18.18f, 21.02f)
                lineTo(12f, 17.77f)
                lineTo(5.82f, 21.02f)
                lineTo(7f, 14.14f)
                lineTo(2f, 9.27f)
                lineTo(8.91f, 8.26f)
                close()
            }
        }.build()
    }

    val StarFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "StarFilled",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(fill = fillColor) {
                moveTo(12f, 2f)
                lineTo(15.09f, 8.26f)
                lineTo(22f, 9.27f)
                lineTo(17f, 14.14f)
                lineTo(18.18f, 21.02f)
                lineTo(12f, 17.77f)
                lineTo(5.82f, 21.02f)
                lineTo(7f, 14.14f)
                lineTo(2f, 9.27f)
                lineTo(8.91f, 8.26f)
                close()
            }
        }.build()
    }

    // ── Settings ────────────────────────────────────────────────────────────

    val Settings: ImageVector by lazy {
        ImageVector.Builder(
            name = "Settings",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Centre circle (gear centre)
                addOval(9f, 9f, 6f)
                // Gear outline (simplified)
                moveTo(19.4f, 15f)
                curveTo(19.13f, 15.61f, 19.25f, 16.33f, 19.72f, 16.8f)
                lineTo(19.79f, 16.87f)
                curveTo(20.16f, 17.24f, 20.38f, 17.74f, 20.38f, 18.27f)
                curveTo(20.38f, 19.37f, 19.49f, 20.26f, 18.39f, 20.26f)
                curveTo(17.86f, 20.26f, 17.36f, 20.04f, 16.99f, 19.67f)
                lineTo(16.92f, 19.6f)
                curveTo(16.45f, 19.13f, 15.73f, 19.01f, 15.12f, 19.28f)
                curveTo(14.53f, 19.53f, 14.13f, 20.1f, 14.12f, 20.75f)
                lineTo(14.12f, 21f)
                curveTo(14.12f, 22.1f, 13.22f, 23f, 12.12f, 23f)
                curveTo(11.02f, 23f, 10.12f, 22.1f, 10.12f, 21f)
                curveTo(10.09f, 20.31f, 9.65f, 19.71f, 9.02f, 19.46f)
                curveTo(8.41f, 19.19f, 7.69f, 19.31f, 7.22f, 19.78f)
                lineTo(7.15f, 19.85f)
                curveTo(6.78f, 20.22f, 6.28f, 20.44f, 5.75f, 20.44f)
                curveTo(4.65f, 20.44f, 3.75f, 19.55f, 3.75f, 18.45f)
                curveTo(3.75f, 17.92f, 3.98f, 17.42f, 4.35f, 17.05f)
                lineTo(4.42f, 16.98f)
                curveTo(4.89f, 16.51f, 5.01f, 15.79f, 4.74f, 15.18f)
                curveTo(4.49f, 14.59f, 3.92f, 14.19f, 3.27f, 14.18f)
                lineTo(3f, 14.18f)
                curveTo(1.9f, 14.18f, 1f, 13.28f, 1f, 12.18f)
                curveTo(1f, 11.08f, 1.9f, 10.18f, 3f, 10.18f)
                curveTo(3.69f, 10.15f, 4.29f, 9.71f, 4.54f, 9.08f)
                curveTo(4.81f, 8.47f, 4.69f, 7.75f, 4.22f, 7.28f)
                lineTo(4.15f, 7.21f)
                curveTo(3.78f, 6.84f, 3.56f, 6.34f, 3.56f, 5.81f)
                curveTo(3.56f, 4.71f, 4.45f, 3.82f, 5.55f, 3.82f)
                curveTo(6.08f, 3.82f, 6.58f, 4.04f, 6.95f, 4.41f)
                lineTo(7.02f, 4.48f)
                curveTo(7.49f, 4.95f, 8.21f, 5.07f, 8.82f, 4.8f)
                curveTo(9.41f, 4.55f, 9.81f, 3.98f, 9.82f, 3.33f)
                lineTo(9.82f, 3f)
                curveTo(9.82f, 1.9f, 10.72f, 1f, 11.82f, 1f)
                curveTo(12.92f, 1f, 13.82f, 1.9f, 13.82f, 3f)
                curveTo(13.85f, 3.69f, 14.29f, 4.29f, 14.92f, 4.54f)
                curveTo(15.53f, 4.81f, 16.25f, 4.69f, 16.72f, 4.22f)
                lineTo(16.79f, 4.15f)
                curveTo(17.16f, 3.78f, 17.66f, 3.56f, 18.19f, 3.56f)
                curveTo(19.29f, 3.56f, 20.19f, 4.45f, 20.19f, 5.55f)
                curveTo(20.19f, 6.08f, 19.96f, 6.58f, 19.59f, 6.95f)
                lineTo(19.52f, 7.02f)
                curveTo(19.05f, 7.49f, 18.93f, 8.21f, 19.2f, 8.82f)
                curveTo(19.45f, 9.41f, 20.02f, 9.81f, 20.67f, 9.82f)
                lineTo(21f, 9.82f)
                curveTo(22.1f, 9.82f, 23f, 10.72f, 23f, 11.82f)
                curveTo(23f, 12.92f, 22.1f, 13.82f, 21f, 13.82f)
                curveTo(20.31f, 13.85f, 19.71f, 14.29f, 19.46f, 14.92f)
                close()
            }
        }.build()
    }

    // ── ChevronRight ────────────────────────────────────────────────────────

    val ChevronRight: ImageVector by lazy {
        ImageVector.Builder(
            name = "ChevronRight",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9f, 18f)
                lineTo(15f, 12f)
                lineTo(9f, 6f)
            }
        }.build()
    }

    val ChevronLeft: ImageVector by lazy {
        ImageVector.Builder(
            name = "ChevronLeft",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 18f)
                lineTo(9f, 12f)
                lineTo(15f, 6f)
            }
        }.build()
    }

    // ── Call State Icons ────────────────────────────────────────────────────

    val CallMissed: ImageVector by lazy {
        ImageVector.Builder(
            name = "CallMissed",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Arrow pointing down-left
                moveTo(22f, 2f)
                lineTo(12f, 12f)
                lineTo(2f, 2f)
                // Horizontal line at arrow base
                moveTo(2f, 2f)
                lineTo(2f, 8f)
                moveTo(22f, 2f)
                lineTo(22f, 8f)
            }
        }.build()
    }

    val CallIncoming: ImageVector by lazy {
        ImageVector.Builder(
            name = "CallIncoming",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Arrow pointing down-left into position
                moveTo(16f, 2f)
                lineTo(16f, 8f)
                lineTo(22f, 8f)
                moveTo(22f, 2f)
                lineTo(16f, 8f)
                // Phone base
                moveTo(17f, 15f)
                lineTo(17f, 17f)
                curveTo(17f, 17.55f, 16.55f, 18f, 16f, 18f)
                curveTo(10.48f, 18f, 6f, 13.52f, 6f, 8f)
                curveTo(6f, 7.45f, 6.45f, 7f, 7f, 7f)
                lineTo(9f, 7f)
            }
        }.build()
    }

    val CallOutgoing: ImageVector by lazy {
        ImageVector.Builder(
            name = "CallOutgoing",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Arrow pointing up-right
                moveTo(22f, 8f)
                lineTo(22f, 2f)
                lineTo(16f, 2f)
                moveTo(16f, 8f)
                lineTo(22f, 2f)
                // Phone base
                moveTo(17f, 15f)
                lineTo(17f, 17f)
                curveTo(17f, 17.55f, 16.55f, 18f, 16f, 18f)
                curveTo(10.48f, 18f, 6f, 13.52f, 6f, 8f)
                curveTo(6f, 7.45f, 6.45f, 7f, 7f, 7f)
                lineTo(9f, 7f)
            }
        }.build()
    }

    // ── Media Controls ──────────────────────────────────────────────────────

    val Record: ImageVector by lazy {
        ImageVector.Builder(
            name = "Record",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth
            ) {
                // Record circle
                addOval(4f, 4f, 16f)
            }
            path(fill = SolidColor(Color(0xFFD32F2F))) {
                // Inner filled dot
                addOval(8f, 8f, 8f)
            }
        }.build()
    }

    val Pause: ImageVector by lazy {
        ImageVector.Builder(
            name = "Pause",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round
            ) {
                // Left bar
                moveTo(9f, 5f)
                lineTo(9f, 19f)
                // Right bar
                moveTo(15f, 5f)
                lineTo(15f, 19f)
            }
        }.build()
    }

    val Play: ImageVector by lazy {
        ImageVector.Builder(
            name = "Play",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7f, 4f)
                lineTo(20f, 12f)
                lineTo(7f, 20f)
                close()
            }
        }.build()
    }

    val Stop: ImageVector by lazy {
        ImageVector.Builder(
            name = "Stop",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 4f)
                lineTo(18f, 4f)
                curveTo(19.1f, 4f, 20f, 4.9f, 20f, 6f)
                lineTo(20f, 18f)
                curveTo(20f, 19.1f, 19.1f, 20f, 18f, 20f)
                lineTo(6f, 20f)
                curveTo(4.9f, 20f, 4f, 19.1f, 4f, 18f)
                lineTo(4f, 6f)
                curveTo(4f, 4.9f, 4.9f, 4f, 6f, 4f)
                close()
            }
        }.build()
    }

    val Share: ImageVector by lazy {
        ImageVector.Builder(
            name = "Share",
            defaultWidth = defaultSize,
            defaultHeight = defaultSize,
            viewportWidth = viewportSize,
            viewportHeight = viewportSize
        ).apply {
            path(
                stroke = strokeColor,
                strokeLineWidth = defaultStrokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Upload arrow
                moveTo(12f, 3f)
                lineTo(12f, 15f)
                moveTo(7f, 8f)
                lineTo(12f, 3f)
                lineTo(17f, 8f)
                // Tray
                moveTo(20f, 16f)
                lineTo(20f, 19f)
                curveTo(20f, 20.1f, 19.1f, 21f, 18f, 21f)
                lineTo(6f, 21f)
                curveTo(4.9f, 21f, 4f, 20.1f, 4f, 19f)
                lineTo(4f, 16f)
            }
        }.build()
    }

    // ── Helper: addOval ─────────────────────────────────────────────────────

    /**
     * Draws a circle approximated with four cubic Bézier curves.
     *
     * @param x      Top-left X of the bounding box.
     * @param y      Top-left Y of the bounding box.
     * @param size   Diameter of the circle.
     */
    private fun androidx.compose.ui.graphics.vector.PathBuilder.addOval(
        x: Float,
        y: Float,
        size: Float
    ) {
        val r = size / 2f
        val cx = x + r
        val cy = y + r
        val k = r * 0.5523f // magic number for cubic Bézier circle approximation

        moveTo(cx, y)
        curveTo(cx + k, y, x + size, cy - k, x + size, cy)
        curveTo(x + size, cy + k, cx + k, y + size, cx, y + size)
        curveTo(cx - k, y + size, x, cy + k, x, cy)
        curveTo(x, cy - k, cx - k, y, cx, y)
        close()
    }
}
