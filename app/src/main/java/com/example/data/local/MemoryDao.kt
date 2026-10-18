package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memory_webs ORDER BY createdAt DESC")
    fun getAllMemoryWebs(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memory_webs WHERE id = :id LIMIT 1")
    suspend fun getMemoryWebById(id: String): MemoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryWeb(item: MemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MemoryEntity>)

    @Query("DELETE FROM memory_webs WHERE id = :id")
    suspend fun deleteMemoryWebById(id: String)
}
