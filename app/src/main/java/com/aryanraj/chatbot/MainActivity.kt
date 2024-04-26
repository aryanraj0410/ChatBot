package com.aryanraj.chatbot
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.aryanraj.chatbot.ui.theme.BackgroundImageChangingScreen
import com.aryanraj.chatbot.ui.theme.ChatBotTheme
import com.aryanraj.chatbot.ui.theme.ColorCode1
import com.aryanraj.chatbot.ui.theme.ColorCode2
import com.aryanraj.chatbot.ui.theme.DarkGrey
import com.aryanraj.chatbot.ui.theme.DarkTeal
import com.aryanraj.chatbot.ui.theme.PurpleDark
import com.aryanraj.chatbot.ui.theme.Teal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {

    private val uriState = MutableStateFlow("")

    private val imagePicker =
        registerForActivityResult<PickVisualMediaRequest, Uri>(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                uriState.update { uri.toString() }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatBotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = White,
                    contentColor = Black
                ) {
                    TopBar()
                }
            }
        }
    }

    @Composable
    fun TopBar(){
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Teal)
                        .height(50.dp)
                        .padding(horizontal = 16.dp,)
                        .padding(top = 5.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                    {
                        Image(painter = painterResource(id = R.drawable.chatbot_12441094),
                            contentDescription = "Icon Image",
                            modifier = Modifier.padding(5.dp))
                        Text(
                            text = "AI ChatBot",
                            fontSize = 22.sp,
                            color = White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        ) {
            ChatScreen(paddingValues = it)
        }

    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChatScreen(paddingValues: PaddingValues) {
        val chaViewModel = viewModel<ChatViewModel>()
        val chatState = chaViewModel.chatState.collectAsState().value

        val bitmap = getBitmap()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                reverseLayout = true
            ) {
                itemsIndexed(chatState.chatList) { index, chat ->
                    if (chat.isFromUser) {
                        UserChatItem(
                            prompt = chat.prompt, bitmap = chat.bitmap
                        )
                    } else {
                        ModelChatItem(response = chat.prompt)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 4.dp, end = 4.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    bitmap?.let {
                        Image(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 2.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentDescription = "picked image",
                            contentScale = ContentScale.Crop,
                            bitmap = it.asImageBitmap()
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                           // .padding(3.dp)
                            .clickable {
                                imagePicker.launch(
                                    PickVisualMediaRequest
                                        .Builder()
                                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        .build()
                                )
                            },
                        imageVector = Icons.Rounded.AddPhotoAlternate,
                        contentDescription = "Add Photo",
                        tint = Teal
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f),
                    value = chatState.prompt,
                    shape = RoundedCornerShape(9.dp),
                    onValueChange = {
                        chaViewModel.onEvent(ChatUiEvent.UpdatePrompt(it))
                    },
                    placeholder = {
                        Text(text = "Reveal your hidden wishes.")
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            chaViewModel.onEvent(ChatUiEvent.SendPrompt(chatState.prompt, bitmap))
                            uriState.update { "" }
                        },
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Send prompt",
                    tint = Teal
                )
            }
        }
    }

    @Composable
    fun UserChatItem(prompt: String, bitmap: Bitmap?) {
        Column(
            modifier = Modifier.padding(start = 100.dp, bottom = 16.dp)
        ) {
            bitmap?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    bitmap = it.asImageBitmap()
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkGrey)
                    .padding(16.dp),
                text = prompt,
                fontSize = 17.sp,
                color = White
            )
        }
    }

    @Composable
    fun ModelChatItem(response: String) {
        Column(
            modifier = Modifier.padding(end = 100.dp, bottom = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkTeal)
                    .padding(16.dp),
                text = response,
                fontSize = 17.sp,
                color = White
            )
        }
    }

    @Composable
    private fun getBitmap(): Bitmap? {
        val uri = uriState.collectAsState().value

        val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .size(Size.ORIGINAL)
                .build()
        ).state
        if (imageState is AsyncImagePainter.State.Success) {
            return imageState.result.drawable.toBitmap()
        }
        return null
    }
}