package com.capx.dialer.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capx.dialer.core.ui.theme.DialerTheme
import kotlin.math.abs

/**
 * Palette of soft gradient pairs used for initials avatars. The pair is chosen
 * deterministically from the contact name so a given person always gets the
 * same colour.
 */
private val avatarGradients: List<Pair<Color, Color>> = listOf(
    Color(0xFF0A7E8C) to Color(0xFF30B0C7), // teal
    Color(0xFF5856D6) to Color(0xFF8E8CFF), // indigo
    Color(0xFFFF9500) to Color(0xFFFFB340), // amber
    Color(0xFF34C759) to Color(0xFF5BD97A), // green
    Color(0xFFFF375F) to Color(0xFFFF6482), // pink
    Color(0xFF5AC8FA) to Color(0xFF64D2FF), // sky
    Color(0xFFAF52DE) to Color(0xFFC77DFF), // purple
    Color(0xFFFF6B35) to Color(0xFFFF8C5A)  // coral
)

private fun initialsFor(name: String, fallbackNumber: String): String {
    val trimmed = name.trim()
    if (trimmed.isEmpty()) {
        // Use the last digit-ish char of the number, else a phone glyph.
        return fallbackNumber.lastOrNull { it.isLetterOrDigit() }?.uppercase() ?: "#"
    }
    val parts = trimmed.split(' ', '-', '.').filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts.first().first()}${parts.last().first()}".uppercase()
        else -> parts.first().take(2).uppercase()
    }
}

private fun gradientFor(key: String): Pair<Color, Color> {
    val idx = abs(key.hashCode()) % avatarGradients.size
    return avatarGradients[idx]
}

/**
 * Circular initials avatar with a deterministic gradient background.
 *
 * @param name Contact display name (may be blank for unknown numbers).
 * @param number Fallback used for initials/colour when [name] is blank.
 * @param size Diameter of the avatar.
 */
@Composable
fun DialerAvatar(
    name: String,
    number: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val key = name.ifBlank { number }.ifBlank { "?" }
    val (start, end) = gradientFor(key)
    val initials = initialsFor(name, number)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(start, end)))
    ) {
        androidx.compose.material3.Text(
            text = initials,
            style = DialerTheme.typography.titleSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = (size.value * 0.36f).sp
            )
        )
    }
}
