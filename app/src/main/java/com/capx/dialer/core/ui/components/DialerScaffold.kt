package com.capx.dialer.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.capx.dialer.core.ui.theme.DialerTheme

/**
 * Large iOS/One UI-style screen title used at the top of each tab.
 *
 * @param title The screen title.
 * @param subtitle Optional secondary line under the title.
 * @param trailing Optional trailing content (e.g. an action icon).
 */
@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val colors = DialerTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 12.dp, top = 20.dp, bottom = 8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(
                text = title,
                style = DialerTheme.typography.displaySmall.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            if (subtitle != null) {
                androidx.compose.material3.Text(
                    text = subtitle,
                    style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary)
                )
            }
        }
        if (trailing != null) {
            Spacer(Modifier.width(8.dp))
            trailing()
        }
    }
}

/** Small uppercase section label used to break up grouped lists. */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(
        text = text,
        style = DialerTheme.typography.caption.copy(
            color = DialerTheme.colors.textSecondary,
            fontWeight = FontWeight.SemiBold
        ),
        modifier = modifier.padding(start = 20.dp, top = 12.dp, bottom = 4.dp)
    )
}
