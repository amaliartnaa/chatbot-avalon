package com.example.chatbotavalon.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.chatbotavalon.data.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun tambahUser(user: User)

    @Query("SELECT * FROM User")
    suspend fun ambilSemuaUser(): List<User>

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun ambilUserByEmail(email: String): User?

    @Delete
    suspend fun hapusUser(user: User)
}
