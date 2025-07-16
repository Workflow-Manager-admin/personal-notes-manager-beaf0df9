package com.example.notesfrontend.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * PUBLIC_INTERFACE
 * Data model for a Note. Room Entity.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    var title: String,
    var content: String,
    val created: Long = System.currentTimeMillis(),
    var updated: Long = System.currentTimeMillis()
)
