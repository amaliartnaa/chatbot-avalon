package com.example.chatbotavalon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chatbotavalon.data.dao.PesanDao
import com.example.chatbotavalon.data.dao.TopikDao
import com.example.chatbotavalon.data.dao.UserDao
import com.example.chatbotavalon.data.entity.Pesan
import com.example.chatbotavalon.data.entity.Topik
import com.example.chatbotavalon.data.entity.User

@Database(entities = [User::class, Topik::class, Pesan::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun topikDao(): TopikDao
    abstract fun pesanDao(): PesanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chatbot_avalon_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
