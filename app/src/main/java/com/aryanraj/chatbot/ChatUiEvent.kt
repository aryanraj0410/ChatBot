package com.aryanraj.chatbot

import android.graphics.Bitmap

sealed class ChatUiEvent {
    data class SendPrompt(
        val prompt: String,
        val bitmap: Bitmap?
    ) : ChatUiEvent()
}
