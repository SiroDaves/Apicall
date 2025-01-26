package com.example.wolt


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun VenueListScreen(
    modifier: Modifier = Modifier,
    viewModel: VenueViewModel = hiltViewModel(),
) {
    // Collect state from the ViewModel
    val venues by viewModel.venues.collectAsState(initial = emptyList())
    val loading by viewModel.loading.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = null)

    // Start fetching venues only once
    LaunchedEffect(key1 = Unit) {
        viewModel.startVenueFetchCycle()
    }

    // Venue list screen layout
    Column(modifier = modifier.padding(16.dp)) {
        when {
            loading -> {
                LoadingState()
            }
            error != null -> {
                ErrorState(errorMessage = error!!) {

                }
            }
            venues.isEmpty() -> {
                EmptyState()
            }
            else -> {
                LazyColumn {
                    items(venues) { venue ->
                        VenueItem(venue = venue)
                    }
                }
            }
        }
    }
}

@Composable
fun VenueItem(venue: Venue) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Id: ${venue.id}", fontWeight = FontWeight.Bold)
        Text(text = "Name: ${venue.name}")
        Text(text = "Short Description: ${venue.short_description}")
        Text(text = "Image URL: ${venue.url}")
    }
}

@Composable
fun LoadingState() {
    Text(text = "Loading...", modifier = Modifier.padding(16.dp))
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun EmptyState() {
    Text(text = "No venues available.", modifier = Modifier.padding(16.dp))
}
