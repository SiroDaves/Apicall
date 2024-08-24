package com.example.wolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wolt.presentation.screens.VenueListScreen
import com.example.wolt.presentation.theme.WoltTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoltTheme {
                VenueListScreen(viewModel = hiltViewModel())
                }
            }
        }
    }


