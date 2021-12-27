package com.example.calculator.domain

import com.example.calculator.domain.entity.HistoryItem

interface HistoryRepository {
    suspend fun add(historyItem: HistoryItem)

    suspend fun getAll(): List<HistoryItem>
}