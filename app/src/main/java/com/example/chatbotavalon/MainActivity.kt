package com.example.chatbotavalon

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbotavalon.data.dao.PesanDao
import com.example.chatbotavalon.data.dao.TopikDao
import com.example.chatbotavalon.data.dao.UserDao
import kotlinx.coroutines.*

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
    private lateinit var database: AppDatabase
    private lateinit var topikDao: TopikDao
    private lateinit var pesanDao: PesanDao
    private lateinit var userDao: UserDao

    @SuppressLint("WrongViewCast")
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

        setupViewsAndAdapters()
        setupToolbarAndDrawer()

        plusButton = findViewById(R.id.plusButton)
        plusButton.setOnClickListener {
            showAttachmentOptions()
        }

        plusButton = findViewById(R.id.addTopicButton)
        plusButton.setOnClickListener {
            showNewTopicDialog()
        }

        database = AppDatabase.getDatabase(this)
        topikDao = database.topikDao()
        pesanDao = database.pesanDao()
        userDao = database.userDao()

        val sidebar = findViewById<LinearLayout>(R.id.sidebar)

        val userId = 1
        CoroutineScope(Dispatchers.IO).launch {
            val topikList = topikDao.ambilTopikByUser(userId)
            withContext(Dispatchers.Main) {
                topikList.forEach { topik ->
                    val textView = TextView(this@MainActivity)
                    textView.text = topik.nama
                    textView.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            val pesanList = pesanDao.ambilPesanByTopik(topik.id)
                            withContext(Dispatchers.Main) {
                                pesanList.forEach { Log.d("Pesan", it.isi) }
                            }
                        }
                    }
                    sidebar.addView(textView)
                }
            }
        }
    }

    private fun setupViewsAndAdapters() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)

        chatAdapter = ChatAdapter(chatList) { chatMessage, position ->
            showEditOrDeleteDialog(position)
        }

        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        drawerLayout = findViewById(R.id.drawerLayout)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        historyAdapter = HistoryAdapter(
            historyList,
            onEditClick = { selectedItem ->
                showEditDialog(selectedItem)
            },
            onDeleteClick = { selectedItem ->
                deleteHistoryItem(selectedItem)
            }
        )
        historyRecyclerView.adapter = historyAdapter

        sendButton.setOnClickListener {
            val userMessage = editText.text.toString()
            if (userMessage.isNotBlank()) {
                sendMessage(userMessage, recyclerView, editText)
            }
        }
    }

    private fun setupToolbarAndDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                historyRecyclerView.visibility = View.VISIBLE
            }
            override fun onDrawerClosed(drawerView: View) {
                historyRecyclerView.visibility = View.GONE
            }
            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    private fun sendMessage(message: String, recyclerView: RecyclerView, editText: EditText) {
        chatList.add(ChatMessage(message, true))
        chatAdapter.notifyItemInserted(chatList.size - 1)
        historyList.add(message)
        historyAdapter.notifyItemInserted(historyList.size - 1)
        editText.text.clear()
        recyclerView.scrollToPosition(chatList.size - 1)

        Handler(Looper.getMainLooper()).postDelayed({
            val botReply = getBotReply(message)
            chatList.add(ChatMessage(botReply, false))
            chatAdapter.notifyItemInserted(chatList.size - 1)
            recyclerView.scrollToPosition(chatList.size - 1)
        }, 1000)
    }

    private fun getBotReply(message: String): String {
        return when {
            message.contains("halo", true) -> "Halo! Ada yang bisa saya bantu?"
            message.contains("apa kabar", true) -> "Saya baik-baik saja! Kamu?"
            message.contains("bye", true) -> "Sampai jumpa!"
            else -> "Maaf, saya tidak mengerti."
        }
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

    private fun showEditOrDeleteDialog(position: Int) {
        val chatMessage = chatList[position]
        AlertDialog.Builder(this)
            .setTitle("Edit or Delete")
            .setItems(arrayOf("Edit", "Delete")) { _, which ->
                when (which) {
                    0 -> showEditDialog(chatMessage.message)
                    1 -> {
                        chatList.removeAt(position)
                        chatAdapter.notifyItemRemoved(position)
                    }
                }
            }
            .show()
    }

    private fun showEditDialog(item: String) {
        val editText = EditText(this)
        editText.setText(item)

        AlertDialog.Builder(this)
            .setTitle("Edit History")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newValue = editText.text.toString()
                val index = historyList.indexOf(item)
                if (index != -1) {
                    historyList[index] = newValue
                    historyAdapter.notifyItemChanged(index)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteHistoryItem(item: String) {
        val index = historyList.indexOf(item)
        if (index != -1) {
            historyList.removeAt(index)
            historyAdapter.notifyItemRemoved(index)
        }
    }

    private fun showNewTopicDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Tambah Topik Baru")
            .setView(input)
            .setPositiveButton("Tambah") { _, _ ->
                val topic = input.text.toString()
                if (topic.isNotBlank()) {
                    addNewTopicToHistory(topic)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun addNewTopicToHistory(topic: String) {
        historyList.add(topic)
        historyAdapter.notifyItemInserted(historyList.size - 1)
    }
}
