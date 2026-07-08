package com.capx.dialer.feature.dialpad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.domain.model.Contact
import com.capx.dialer.core.ui.animation.pressAnimation
import com.capx.dialer.core.ui.components.DialerAvatar
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DialerTheme

private data class Key(val digit: String, val letters: String = "")

private val KEYPAD = listOf(
    Key("1"), Key("2", "ABC"), Key("3", "DEF"),
    Key("4", "GHI"), Key("5", "JKL"), Key("6", "MNO"),
    Key("7", "PQRS"), Key("8", "TUV"), Key("9", "WXYZ"),
    Key("*"), Key("0", "+"), Key("#")
)

@Composable
fun DialpadScreen(
    viewModel: DialpadViewModel = hiltViewModel(),
    onPlaceCall: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = DialerTheme.colors

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is DialpadUiEvent.RequestCall -> onPlaceCall(event.number)
                is DialpadUiEvent.ShowSnackbar -> onShowSnackbar(event.message)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── Suggestions / number preview area ────────────────────────────
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (state.suggestedContacts.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.suggestedContacts, key = { it.id }) { contact ->
                        SuggestionRow(
                            contact = contact,
                            onClick = { viewModel.onSuggestionClick(contact.phoneNumber) }
                        )
                    }
                }
            }
        }

        // ── Typed number ─────────────────────────────────────────────────
        Text(
            text = state.currentNumber,
            style = DialerTheme.typography.display.copy(
                color = colors.textPrimary,
                fontWeight = FontWeight.Light
            ),
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        // ── Keypad ───────────────────────────────────────────────────────
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            KEYPAD.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { key ->
                        DialpadKey(
                            key = key,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onDigitPress(key.digit[0]) }
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }

        // ── Action row: call + backspace ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))

            // Call button (centre)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(68.dp)
                        .pressAnimation(onPress = { viewModel.onCallClick() })
                        .clip(CircleShape)
                        .background(colors.callGreen)
                ) {
                    androidx.compose.foundation.Image(
                        imageVector = DialerIcons.PhoneFilled,
                        contentDescription = "Call",
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }

            // Backspace (right)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                if (state.currentNumber.isNotEmpty()) {
                    val haptic = LocalHapticFeedback.current
                    val interaction = androidx.compose.runtime.remember { MutableInteractionSource() }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = interaction,
                                indication = null,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.onBackspaceClick()
                                }
                            )
                    ) {
                        androidx.compose.foundation.Image(
                            imageVector = DialerIcons.Backspace,
                            contentDescription = "Delete",
                            modifier = Modifier.size(26.dp),
                            colorFilter = ColorFilter.tint(colors.textSecondary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialpadKey(
    key: Key,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = DialerTheme.colors
    val haptic = LocalHapticFeedback.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(6.dp)
            .aspectRatio(1.35f)
            .pressAnimation(
                onPress = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            )
            .clip(CircleShape)
            .background(colors.surface)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = key.digit,
                style = DialerTheme.typography.headline.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 30.sp
                )
            )
            if (key.letters.isNotEmpty()) {
                Text(
                    text = key.letters,
                    style = DialerTheme.typography.caption.copy(
                        color = colors.textSecondary,
                        letterSpacing = 2.sp,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun SuggestionRow(contact: Contact, onClick: () -> Unit) {
    val colors = DialerTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(DialerTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        DialerAvatar(name = contact.name, number = contact.phoneNumber, size = 40.dp)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.name.ifBlank { contact.phoneNumber },
                style = DialerTheme.typography.body.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )
            if (contact.name.isNotBlank()) {
                Text(
                    text = contact.phoneNumber,
                    style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary),
                    maxLines = 1
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.callGreen.copy(alpha = 0.14f))
        ) {
            androidx.compose.foundation.Image(
                imageVector = DialerIcons.Phone,
                contentDescription = "Call ${contact.name}",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(colors.callGreen)
            )
        }
    }
}

@Composable
private fun Text(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = androidx.compose.material3.Text(
    text = text,
    style = style,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign
)
