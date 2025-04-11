package com.aryanraj.chatbot.screens

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.aryanraj.chatbot.ChatUiEvent
import com.aryanraj.chatbot.ChatViewModel
import com.aryanraj.chatbot.ui.theme.darkolive
import com.aryanraj.chatbot.ui.theme.limegreen
import com.aryanraj.chatbot.ui.theme.olivegreen
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiHelper(
    navController: NavController,
    uriState: StateFlow<String>,
    imagePicker: ActivityResultLauncher<PickVisualMediaRequest>
) {
    val viewModel: ChatViewModel = viewModel()
    val chatState = viewModel.chatState.collectAsState().value
    var prompt by remember { mutableStateOf("") }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val currentBitmap = rememberBitmapFromUri(uriState)

    // Update selected bitmap whenever URI changes
    LaunchedEffect(currentBitmap) {
        if (currentBitmap != null) {
            selectedBitmap = currentBitmap
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("AI Assistant", color = Color.White, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("MainScreen") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = olivegreen),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
        },
        containerColor = darkolive,
        bottomBar = {
            Row(
                Modifier
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        imagePicker.launch(
                            PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                .build()
                        )
                    }
                ) {
                    Icon(Icons.Rounded.Image, contentDescription = "Upload Image", tint = olivegreen)
                }

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("Ask me anything...") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = olivegreen,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                IconButton(
                    onClick = {
                        selectedBitmap?.let { bmp ->
                            viewModel.onEvent(ChatUiEvent.SendPrompt(prompt, bmp))
                            prompt = ""
                            selectedBitmap = null
                        }
                    }
                ) {
                    Icon(Icons.Rounded.Send, contentDescription = "Send", tint = olivegreen)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = true
        ) {
            items(chatState.chatList) { chat ->
                val isUser = chat.isFromUser
                val bubbleColor = if (isUser) olivegreen else limegreen

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = bubbleColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = chat.prompt,
                                color = Color.White,
                                fontSize = 16.sp
                            )

                            if (isUser && chat.bitmap != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Image(
                                    bitmap = chat.bitmap.asImageBitmap(),
                                    contentDescription = "Sent Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .height(180.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberBitmapFromUri(uriState: StateFlow<String>): Bitmap? {
    val uri = uriState.collectAsState().value
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .size(Size.ORIGINAL)
            .build()
    )
    val state = painter.state
    return if (state is AsyncImagePainter.State.Success) {
        state.result.drawable.toBitmap()
    } else {
        null
    }
}
