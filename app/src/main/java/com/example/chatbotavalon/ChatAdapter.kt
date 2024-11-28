package com.example.chatbotavalon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatList: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userTextView: TextView = view.findViewById(R.id.userMessage)
        val botTextView: TextView = view.findViewById(R.id.botMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        if (chat.isUser) {
            holder.userTextView.text = chat.message
            holder.userTextView.visibility = View.VISIBLE
            holder.botTextView.visibility = View.GONE
        } else {
            holder.botTextView.text = chat.message
            holder.botTextView.visibility = View.VISIBLE
            holder.userTextView.visibility = View.GONE
        }
    }
}

