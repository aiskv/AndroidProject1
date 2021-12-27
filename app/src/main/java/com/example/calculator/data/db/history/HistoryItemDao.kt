package com.example.calculator.data.db.history

import androidx.room.*
import com.example.calculator.data.db.typeConverters.LocalDateTimeConverter

@Dao
interface HistoryItemDao {

    @Insert
    suspend fun insert(historyItemEntity: HistoryItemEntity)

    @Delete
    suspend fun delete(historyItemEntity: HistoryItemEntity)

    @Query("SELECT * FROM history_item_entity")
    suspend fun getAll(): List<HistoryItemEntity>
}