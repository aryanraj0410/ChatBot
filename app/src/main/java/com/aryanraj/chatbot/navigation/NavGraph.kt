package com.aryanraj.chatbot.navigation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryanraj.chatbot.screens.GetStarted
import com.aryanraj.chatbot.screens.MainScreen
import com.aryanraj.chatbot.screens.AiHelper
import com.aryanraj.chatbot.screens.MriScanner
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NavGraph(navController: NavHostController = rememberNavController(),
             uriState: StateFlow<String>,
             imagePicker: ActivityResultLauncher<PickVisualMediaRequest>
) {

    NavHost(navController = navController, startDestination = "GetStarted") {

        composable("GetStarted") {
            GetStarted(navController)
        }

        composable("MainScreen") {
            MainScreen(navController)
        }

       composable("AiHelper") {
                    AiHelper(
                        navController = navController,
                        uriState = uriState,
                        imagePicker = imagePicker
                    )
                }

        composable("MriScanner") {
            MriScanner(navController)
        }
    }
}
