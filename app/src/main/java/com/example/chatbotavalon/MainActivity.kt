package com.example.chatbotavalon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
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

    private lateinit var plusButton: ImageButton
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickFileLauncher: ActivityResultLauncher<Intent>
    private lateinit var recordVoiceLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val fileUri: Uri? = result.data?.data
                handleFile("Image selected: $fileUri")
            }
        }
        pickFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val fileUri: Uri? = result.data?.data
                handleFile("File selected: $fileUri")
            }
        }

        recordVoiceLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val fileUri: Uri? = result.data?.data
                handleFile("Audio recorded: $fileUri")
            }
        }
        plusButton = findViewById(R.id.plusButton)
        plusButton.setOnClickListener {
            showAttachmentOptions()
        }

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

    private fun showAttachmentOptions() {
        val options = arrayOf("Upload Image", "Upload Document", "Record Voice Note")
        AlertDialog.Builder(this)
            .setTitle("Choose an option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openFilePicker()
                    2 -> recordVoiceNote()
                }
            }
            .show()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        pickFileLauncher.launch(intent)
    }
    private fun recordVoiceNote() {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        recordVoiceLauncher.launch(intent)
    }
    private fun handleFile(message: String) {
        chatList.add(ChatMessage(message, false))
        chatAdapter.notifyItemInserted(chatList.size - 1)
    }
}