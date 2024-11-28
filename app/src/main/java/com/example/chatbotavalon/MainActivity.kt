package com.example.chatbotavalon

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val chatList = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)

        chatAdapter = ChatAdapter(chatList)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val userMessage = editText.text.toString()
            if (userMessage.isNotBlank()) {
                chatList.add(ChatMessage(userMessage, true))
                chatAdapter.notifyItemInserted(chatList.size - 1)

                editText.text.clear()

                recyclerView.scrollToPosition(chatList.size - 1)

                Handler(Looper.getMainLooper()).postDelayed({
                    val botReply = getBotReply(userMessage)
                    chatList.add(ChatMessage(botReply, false))
                    chatAdapter.notifyItemInserted(chatList.size - 1)
                    recyclerView.scrollToPosition(chatList.size - 1)
                }, 2000)
            }
        }
    }

    private fun getBotReply(message: String): String {
        return when {
            message.contains("halo", true) -> "Halo! Ada yang bisa saya bantu?"
            message.contains("apa kabar", true) -> "Saya bot, selalu baik! Kamu?"
            message.contains("bye", true) -> "Sampai jumpa!"
            else -> "Maaf, saya tidak mengerti."
        }
    }
}
