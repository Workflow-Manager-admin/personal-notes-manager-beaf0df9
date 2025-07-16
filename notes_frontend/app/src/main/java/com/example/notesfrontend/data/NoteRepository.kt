package com.example.notesfrontend.data

import androidx.lifecycle.LiveData

/**
 * PUBLIC_INTERFACE
 * Repository for accessing note data.
 */
class NoteRepository(private val dao: NoteDao) {
    fun getAll(): LiveData<List<Note>> = dao.getAll()

    fun search(query: String): LiveData<List<Note>> = dao.search("%$query%")

    suspend fun getById(id: Long): Note? = dao.getById(id)

    suspend fun insert(note: Note): Long = dao.insert(note)

    suspend fun update(note: Note) = dao.update(note)

    suspend fun delete(note: Note) = dao.delete(note)
}
