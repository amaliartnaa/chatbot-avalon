package com.example.chatbotavalon.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.chatbotavalon.data.entity.Pesan

@Dao
interface PesanDao {
    @Insert
    fun tambahPesan(pesan: Pesan)

    @Query("SELECT * FROM Pesan WHERE topikId = :topikId")
    fun ambilPesanByTopik(topikId: Int): List<Pesan>

    @Delete
    fun hapusPesan(pesan: Pesan)
}
