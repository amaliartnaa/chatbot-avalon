package com.example.chatbotavalon.repository

import com.example.chatbotavalon.data.dao.TopikDao
import com.example.chatbotavalon.data.entity.Topik

class TopikRepository(private val topikDao: TopikDao) {

    fun updateTopik(topik: Topik) {
        topikDao.updateTopik(topik)
    }

    fun deleteTopik(topik: Topik) {
        topikDao.deleteTopik(topik)
    }

    fun ambilTopikByUser(userId: Int): List<Topik> {
        return topikDao.ambilTopikByUser(userId)
    }
}