package com.example.chatbotavalon.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.chatbotavalon.data.entity.User

@Dao
interface UserDao {
    @Insert
    fun tambahUser(user: User)

    @Query("SELECT * FROM User")
    fun ambilSemuaUser(): List<User>

    @Delete
    fun hapusUser(user: User)
}
