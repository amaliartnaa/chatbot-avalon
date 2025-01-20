package com.example.chatbotavalon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.chatbotavalon.data.entity.User

@Dao
interface UserDao {
    @Insert
    fun tambahUser(user: User)

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    fun loginUser(email: String, password: String): User?
}