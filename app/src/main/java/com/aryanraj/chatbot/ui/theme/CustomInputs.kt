package com.aryanraj.chatbot.ui.theme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import com.aryanraj.chatbot.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BackgroundImageChangingScreen() {
    val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5,
        R.drawable.image6,
        R.drawable.image7,
        R.drawable.image8

    )
    val coroutineScope = rememberCoroutineScope()
    var currentIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(key1 = Unit) {
        //val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch(Dispatchers.Default) {
            while (true) {
                delay(5000)
                currentIndex = (currentIndex + 1) % images.size
            }
        }
    }
//
//    val currentImage = painterResource(id = images[currentIndex] as Int)
//
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Crossfade(current = currentImage) { images->
//            Image(
//                painter = images,
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
}

fun Crossfade(current: Painter, content: @Composable Any) {

}
