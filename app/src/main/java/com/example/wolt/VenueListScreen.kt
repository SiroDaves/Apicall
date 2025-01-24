package com.example.wolt


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
    // Observe the venues data
    val venues = viewModel.venues.collectAsState(initial = emptyList()).value
    val loading = viewModel.loading.collectAsState().value
    val error = viewModel.error.collectAsState().value

    // This line initializes the Hilt-provided ViewModel for this Composable
    val viewModel: VenueViewModel = hiltViewModel()




    // Start fetching venues when the Composable is launched
    LaunchedEffect(Unit) {
        viewModel.startVenueFetchCycle()
    }

    // Layout for the Venue List
    Column(modifier = modifier.padding(16.dp)) {
        if (loading) {
            LoadingState()
        }

        error?.let {
            ErrorState(it)
        }

        LazyColumn {
            items(venues) { venue ->
                VenueItem(venue = venue)
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
        Text(text = "Image Url: ${venue.url}")
    }
}

@Composable
fun LoadingState() {
    Text(text = "Loading...", modifier = Modifier.padding(16.dp))
}

@Composable
fun ErrorState(errorMessage: String) {
    Text(
        text = errorMessage,
        color = Color.Red,
        modifier = Modifier.padding(16.dp)
    )
}
