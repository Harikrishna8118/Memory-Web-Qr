package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.AmbientMusic
import com.example.data.model.InteractiveFeature
import com.example.data.model.MemoryPhoto
import com.example.data.model.MemoryWebItem
import com.example.data.model.OccasionType
import com.example.data.model.QrStyleConfig
import com.example.data.model.ThemeStyle
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(tableName = "memory_webs")
data class MemoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val recipientName: String,
    val senderName: String,
    val occasion: String,
    val themeStyle: String,
    val milestoneNote: String,
    val heartfeltLetter: String,
    val photosJson: String,
    val ambientMusic: String,
    val interactiveFeaturesJson: String,
    val secretScratchNote: String,
    val qrStyleName: String,
    val createdAt: Long,
    val isSample: Boolean
) {
    fun toModel(): MemoryWebItem {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val photoListType = Types.newParameterizedType(List::class.java, MemoryPhoto::class.java)
        val photosAdapter = moshi.adapter<List<MemoryPhoto>>(photoListType)
        val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
        val featureAdapter = moshi.adapter<List<String>>(stringListType)

        val parsedPhotos = try {
            photosAdapter.fromJson(photosJson) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val parsedFeatures = try {
            val names = featureAdapter.fromJson(interactiveFeaturesJson) ?: emptyList()
            names.mapNotNull { name ->
                InteractiveFeature.values().find { it.name == name }
            }.toSet()
        } catch (e: Exception) {
            emptySet()
        }

        return MemoryWebItem(
            id = id,
            title = title,
            recipientName = recipientName,
            senderName = senderName,
            occasion = OccasionType.values().find { it.name == occasion } ?: OccasionType.CUSTOM,
            themeStyle = ThemeStyle.values().find { it.name == themeStyle } ?: ThemeStyle.ROSE_VELVET,
            milestoneNote = milestoneNote,
            heartfeltLetter = heartfeltLetter,
            photos = parsedPhotos,
            ambientMusic = AmbientMusic.values().find { it.name == ambientMusic } ?: AmbientMusic.ROMANTIC_MELODY,
            interactiveFeatures = parsedFeatures,
            secretScratchNote = secretScratchNote,
            qrStyle = QrStyleConfig(styleName = qrStyleName),
            createdAt = createdAt,
            isSample = isSample
        )
    }

    companion object {
        fun fromModel(item: MemoryWebItem): MemoryEntity {
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val photoListType = Types.newParameterizedType(List::class.java, MemoryPhoto::class.java)
            val photosAdapter = moshi.adapter<List<MemoryPhoto>>(photoListType)
            val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
            val featureAdapter = moshi.adapter<List<String>>(stringListType)

            return MemoryEntity(
                id = item.id,
                title = item.title,
                recipientName = item.recipientName,
                senderName = item.senderName,
                occasion = item.occasion.name,
                themeStyle = item.themeStyle.name,
                milestoneNote = item.milestoneNote,
                heartfeltLetter = item.heartfeltLetter,
                photosJson = photosAdapter.toJson(item.photos),
                ambientMusic = item.ambientMusic.name,
                interactiveFeaturesJson = featureAdapter.toJson(item.interactiveFeatures.map { it.name }),
                secretScratchNote = item.secretScratchNote,
                qrStyleName = item.qrStyle.styleName,
                createdAt = item.createdAt,
                isSample = item.isSample
            )
        }
    }
}
