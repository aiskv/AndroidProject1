package com.example.calculator.di

import android.content.Context
import com.example.calculator.data.HistoryRepositoryImpl
import com.example.calculator.domain.HistoryRepository

object HistoryRepositoryProvider {

    private var repository: HistoryRepository? = null

    fun get(context: Context): HistoryRepository {
        return repository ?: HistoryRepositoryImpl(DatabaseProvider.get(context).historyItemDao)
            .also { repository = it }
    }
}