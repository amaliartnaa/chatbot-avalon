package com.example.chatbotavalon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val historyList: List<String>,
    private val onEditClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val message = historyList[position]
        holder.bind(message, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = historyList.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val editIcon: ImageButton = itemView.findViewById(R.id.editIcon)
        private val deleteIcon: ImageButton = itemView.findViewById(R.id.deleteIcon)

        fun bind(message: String, onEditClick: (String) -> Unit, onDeleteClick: (String) -> Unit) {
            messageTextView.text = message

            editIcon.setOnClickListener {
                onEditClick(message)
            }

            deleteIcon.setOnClickListener {
                onDeleteClick(message)
            }
        }
    }
}
