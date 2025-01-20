package com.example.chatbotavalon.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chatbotavalon.data.entity.Topik

@Dao
interface TopikDao {
    @Insert
    fun tambahTopik(topik: Topik)

    @Query("SELECT * FROM Topik WHERE userId = :userId")
    fun ambilTopikByUser(userId: Int): List<Topik>

    @Update
    fun updateTopik(topik: Topik)

    @Delete
    fun deleteTopik(topik: Topik)
}