package com.aryanraj.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.aryanraj.chatbot.navigation.NavGraph
import com.aryanraj.chatbot.ui.theme.ChatBotTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {

    private val _uriState = MutableStateFlow("")
    private val uriState: StateFlow<String> get() = _uriState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // Register the image picker launcher here using PickVisualMediaRequest
            val imagePicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                uri?.let {
                    _uriState.value = it.toString()
                }
            }

            ChatBotTheme {
                Surface(color = Color.White) {
                    NavGraph(
                        navController = navController,
                        uriState = uriState,
                        imagePicker = imagePicker
                    )
                }
            }
        }
    }
}
