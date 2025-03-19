package com.sudhan.genAl.course.chatbot

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.android.gms.auth.api.identity.Identity
import com.sudhan.genAl.course.chatbot.data.repositories.AuthRepository
import com.sudhan.genAl.course.chatbot.data.repositories.AuthRepositoryImpl
import com.sudhan.genAl.course.chatbot.data.repositories.ChatRepository
import com.sudhan.genAl.course.chatbot.data.repositories.GoogleAuthClient
import com.sudhan.genAl.course.chatbot.data.repositories.PhotoReasoningRepository

object Graph {

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }
    val chatRepository: ChatRepository by lazy {
        ChatRepository()
    }
    val photoReasoningRepository: PhotoReasoningRepository by lazy {
        PhotoReasoningRepository()
    }

    lateinit var googleAuthClient: GoogleAuthClient
    private val config = generationConfig {
        temperature = .7f
    }

    fun generativeModel(modelName: String) = GenerativeModel(
        modelName = modelName,
        apiKey = BuildConfig.apiKey,
        generationConfig = config
    )

    fun provide(context: Context) {
        googleAuthClient = GoogleAuthClient(
            oneTapClient = Identity.getSignInClient(context)
        )
    }
}