package com.example.wolt.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.wolt.data.models.Item
import com.example.wolt.presentation.viewmodel.VenueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueListScreen(
    modifier: Modifier = Modifier,
    viewModel: VenueViewModel = hiltViewModel(),
) {
    // Collect state from ViewModel
    val venues by viewModel.venues.collectAsState(initial = emptyList())
    val loading by viewModel.loading.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = null)

    // Manage favorite state
    var favoriteVenues by remember { mutableStateOf(setOf<String>()) }

    // Start fetching venues
    LaunchedEffect(Unit) {
        viewModel.startVenueFetchCycle()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Venue List")
                }
            )
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding).padding(16.dp)) {
            when {
                loading -> LoadingState()
                error != null -> ErrorState(
                    errorMessage = error!!,
                    onRetry = { viewModel.startVenueFetchCycle() }
                )
                venues.isEmpty() -> EmptyState()
                else -> LazyColumn {
                    items(venues) { venue ->
                        val isFavorite = favoriteVenues.contains(venue.title)
                        VenueItem(
                            venue = venue,
                            isFavorite = isFavorite,
                            onFavoriteClick = {
                                favoriteVenues = if (isFavorite) {
                                    favoriteVenues - venue.title
                                } else {
                                    favoriteVenues + venue.title
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VenueItem(venue: Item, isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Venue Image
        /*Image(
            painter = venue.image.url),
            contentDescription = venue.title,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
        )*/
        AsyncImage(
            model = venue.image.url,
            contentDescription = venue.title,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
        )
        // Venue Details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = venue.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            /*Text(
                text = venue.quantity,
                fontSize = 14.sp,
                //color = Color.Gray
            )*/
        }

        // Favorite Button
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Loading venues...", fontSize = 16.sp, color = Color.Gray)
        }
    }
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
