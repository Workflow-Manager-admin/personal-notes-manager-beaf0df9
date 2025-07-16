package com.example.notesfrontend.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.notesfrontend.data.Note
import com.example.notesfrontend.data.NoteDatabase
import com.example.notesfrontend.data.NoteRepository
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * Shared ViewModel for Notes operations.
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>
    private val _searchQuery = MutableLiveData<String>("")

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = Transformations.switchMap(_searchQuery) { query ->
            if (query.isNullOrBlank()) {
                repository.getAll()
            } else {
                repository.search(query)
            }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}

/**
 * PUBLIC_INTERFACE
 * ViewModel for a single note's detail or edit.
 */
class SingleNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    private val _note = MutableLiveData<Note?>()
    val note: LiveData<Note?> = _note

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
    }

    fun loadNote(id: Long) {
        viewModelScope.launch {
            _note.postValue(repository.getById(id))
        }
    }

    fun insert(note: Note, onDone: (Long) -> Unit) {
        viewModelScope.launch {
            onDone(repository.insert(note))
        }
    }

    fun update(note: Note, onDone: () -> Unit) {
        viewModelScope.launch {
            note.updated = System.currentTimeMillis()
            repository.update(note)
            onDone()
        }
    }

    fun delete(note: Note, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.delete(note)
            onDone()
        }
    }
}
