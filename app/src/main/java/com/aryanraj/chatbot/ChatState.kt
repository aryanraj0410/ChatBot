package com.aryanraj.chatbot

import android.graphics.Bitmap
import com.aryanraj.chatbot.data.Chat

data class ChatState (
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)