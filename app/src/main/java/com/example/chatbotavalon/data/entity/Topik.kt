package com.example.chatbotavalon.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class Topik(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val userId: Int
)
