package com.sudhan.genAl.course.chatbot

import android.app.Application

class ChatBotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}