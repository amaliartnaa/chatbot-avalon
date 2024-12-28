package com.example.chatbotavalon

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val chatList = mutableListOf<ChatMessage>()
    private val historyList = mutableListOf<String>()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var historyRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)

        chatAdapter = ChatAdapter(chatList)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        drawerLayout = findViewById(R.id.drawerLayout)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(generateDummyHistory())
        historyRecyclerView.adapter = historyAdapter

        sendButton.setOnClickListener {
            val userMessage = editText.text.toString()
            if (userMessage.isNotBlank()) {
                chatList.add(ChatMessage(userMessage, true))
                chatAdapter.notifyItemInserted(chatList.size - 1)
                historyList.add(userMessage)
                historyAdapter.notifyItemInserted(historyList.size -1)

                editText.text.clear()

                recyclerView.scrollToPosition(chatList.size - 1)

                Handler(Looper.getMainLooper()).postDelayed({
                    val botReply = getBotReply(userMessage)
                    chatList.add(ChatMessage(botReply, false))
                    chatAdapter.notifyItemInserted(chatList.size - 1)
                    recyclerView.scrollToPosition(chatList.size - 1)
                }, 1000)
            }
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {
                historyRecyclerView.visibility = View.VISIBLE
            }

            override fun onDrawerClosed(drawerView: View) {
                historyRecyclerView.visibility = View.GONE
            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })
    }

    private fun getBotReply(message: String): String {
        return when {
            message.contains("halo", true) -> "Halo! Ada yang bisa saya bantu?"
            message.contains("apa kabar", true) -> "Saya baik-baik saja! Kamu?"
            message.contains("bye", true) -> "Sampai jumpa!"
            else -> "Maaf, saya tidak mengerti."
        }
    }

    private fun generateDummyHistory(): List<String> {
        return listOf(
            "Masakan enak di Indonesia",
            "Klasifikasi IQ",
            "Jenis vision di genshin impact",
            "Bedanya initiator dan sentinel di valorant"
        )
    }
}
