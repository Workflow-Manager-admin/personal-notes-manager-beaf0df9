package com.example.notesfrontend.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * PUBLIC_INTERFACE
 * Room DAO for Note entity.
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updated DESC")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query ORDER BY updated DESC")
    fun search(query: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}
