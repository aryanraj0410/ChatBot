    package com.aryanraj.chatbot.data

    import android.graphics.Bitmap
    import com.google.ai.client.generativeai.BuildConfig
    import com.google.ai.client.generativeai.GenerativeModel
    import com.google.ai.client.generativeai.type.content
    import com.google.ai.client.generativeai.type.generationConfig
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.withContext


    object ChatData {


        private const val apiKey = "AIzaSyAg4klXclGjD2TR6B8hSQJo7wgNMki687I"

        suspend fun getResponse(prompt: String): Chat {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash", apiKey = apiKey
            )

            try {
                val response = withContext(Dispatchers.IO) {
                    generativeModel.generateContent(prompt)
                }

                return Chat(
                    prompt = response.text ?: "error",
                    bitmap = null,
                    isFromUser = false
                )

            } catch (e: Exception) {
                return Chat(
                    prompt = e.message ?: "error",
                    bitmap = null,
                    isFromUser = false
                )
            }

        }

        suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash", apiKey = apiKey
            )

            try {

                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }

                val response = withContext(Dispatchers.IO) {
                    generativeModel.generateContent(inputContent)
                }

                return Chat(
                    prompt = response.text ?: "error",
                    bitmap = null,
                    isFromUser = false
                )

            } catch (e: Exception) {
                return Chat(
                    prompt = e.message ?: "error",
                    bitmap = null,
                    isFromUser = false
                )
            }

        }
    }

