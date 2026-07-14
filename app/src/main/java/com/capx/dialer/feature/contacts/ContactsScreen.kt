package com.capx.dialer.feature.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.capx.dialer.core.domain.model.Contact
import com.capx.dialer.core.ui.components.DialerAvatar
import com.capx.dialer.core.ui.components.DialerDivider
import com.capx.dialer.core.ui.components.DialerSearchBar
import com.capx.dialer.core.ui.components.ScreenHeader
import com.capx.dialer.core.ui.components.SectionLabel
import com.capx.dialer.core.ui.icons.DialerIcons
import com.capx.dialer.core.ui.theme.DialerTheme

@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel = hiltViewModel(),
    onCall: (String) -> Unit,
    onOpenCallLog: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = DialerTheme.colors
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ContactsUiEvent.CallContact -> onCall(event.number)
                is ContactsUiEvent.NavigateToContactDetail -> Unit
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(colors.background)) {
        ScreenHeader(title = "Contacts")

        DialerSearchBar(
            query = state.searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            placeholder = "Search name or number",
            searchIcon = DialerIcons.Search,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )
        Spacer(Modifier.height(4.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colors.primary)
            }
            return@Column
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (state.favorites.isNotEmpty() && state.searchQuery.isBlank()) {
                item { SectionLabel("FAVORITES") }
                items(state.favorites, key = { "fav-${it.id}" }) { contact ->
                    ContactRow(
                        contact = contact,
                        onOpenCallLog = { onOpenCallLog(contact.phoneNumber) },
                        onCall = { viewModel.onCallClick(contact.phoneNumber) },
                        onOpenContact = {
                            com.capx.dialer.core.ui.util.ContactIntents.viewOrCreate(context, contact.phoneNumber)
                        }
                    )
                    DialerDivider(startIndent = 80.dp)
                }
            }

            item { SectionLabel(if (state.searchQuery.isBlank()) "ALL CONTACTS" else "RESULTS") }

            if (state.contacts.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        androidx.compose.material3.Text(
                            text = "No contacts found",
                            style = DialerTheme.typography.body.copy(color = colors.textSecondary)
                        )
                    }
                }
            } else {
                items(state.contacts, key = { it.id }) { contact ->
                    ContactRow(
                        contact = contact,
                        onOpenCallLog = { onOpenCallLog(contact.phoneNumber) },
                        onCall = { viewModel.onCallClick(contact.phoneNumber) },
                        onOpenContact = {
                            com.capx.dialer.core.ui.util.ContactIntents.viewOrCreate(context, contact.phoneNumber)
                        }
                    )
                    DialerDivider(startIndent = 80.dp)
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    contact: Contact,
    onOpenCallLog: () -> Unit,
    onCall: () -> Unit,
    onOpenContact: () -> Unit
) {
    val colors = DialerTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenCallLog)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        DialerAvatar(
            name = contact.name,
            number = contact.phoneNumber,
            size = 48.dp,
            modifier = Modifier.clip(CircleShape).clickable(onClick = onOpenContact)
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(
                text = contact.name.ifBlank { contact.phoneNumber },
                style = DialerTheme.typography.body.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )
            if (contact.name.isNotBlank()) {
                androidx.compose.material3.Text(
                    text = contact.phoneNumber,
                    style = DialerTheme.typography.bodySmall.copy(color = colors.textSecondary),
                    maxLines = 1
                )
            }
        }
        if (contact.isFavorite) {
            androidx.compose.foundation.Image(
                imageVector = DialerIcons.StarFilled,
                contentDescription = "Favorite",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(colors.missedCall)
            )
            Spacer(Modifier.width(10.dp))
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.callGreen.copy(alpha = 0.14f))
                .clickable(onClick = onCall)
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
