package com.example.chatbotavalon.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Topik::class,
        parentColumns = ["id"],
        childColumns = ["topikId"],
        onDelete = ForeignKey.CASCADE
    )],
)

data class Pesan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val isi: String,
    val timestamp: Long = System.currentTimeMillis(),
    val topikId: Int,
    val userId: Int
)
