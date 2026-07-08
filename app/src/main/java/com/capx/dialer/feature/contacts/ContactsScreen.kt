package com.capx.dialer.feature.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel = hiltViewModel(),
    onNavigateToDetail: (Long) -> Unit,
    onCall: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ContactsUiEvent.NavigateToContactDetail -> onNavigateToDetail(event.contactId)
                is ContactsUiEvent.CallContact -> onCall(event.number)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                if (state.favorites.isNotEmpty() && state.searchQuery.isEmpty()) {
                    item { Text("Favorites", modifier = Modifier.padding(vertical = 8.dp)) }
                    items(state.favorites) { contact ->
                        ContactItem(
                            name = contact.name,
                            number = contact.phoneNumber,
                            onClick = { viewModel.onContactClick(contact.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                
                item { Text("All Contacts", modifier = Modifier.padding(vertical = 8.dp)) }
                items(state.contacts) { contact ->
                    ContactItem(
                        name = contact.name,
                        number = contact.phoneNumber,
                        onClick = { viewModel.onContactClick(contact.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactItem(name: String, number: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(text = name)
        Text(text = number)
    }
}
