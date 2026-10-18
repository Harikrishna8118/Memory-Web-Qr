package com.example.data.local

import android.content.Context
import com.example.data.model.MemoryWebItem
import com.example.data.model.SampleMemoryData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemoryRepository(context: Context) {
    private val memoryDao = MemoryDatabase.getDatabase(context).memoryDao()

    val memoryWebs: Flow<List<MemoryWebItem>> = memoryDao.getAllMemoryWebs().map { entities ->
        if (entities.isEmpty()) {
            SampleMemoryData.sampleList
        } else {
            entities.map { it.toModel() }
        }
    }

    suspend fun saveMemoryWeb(item: MemoryWebItem) {
        memoryDao.insertMemoryWeb(MemoryEntity.fromModel(item))
    }

    suspend fun deleteMemoryWeb(id: String) {
        memoryDao.deleteMemoryWebById(id)
    }

    suspend fun getMemoryWebById(id: String): MemoryWebItem? {
        val entity = memoryDao.getMemoryWebById(id)
        return entity?.toModel() ?: SampleMemoryData.sampleList.find { it.id == id }
    }
}
