package com.example.notesfrontend.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesfrontend.data.Note
import com.example.notesfrontend.R

/**
 * PUBLIC_INTERFACE
 * RecyclerView Adapter for Notes list.
 */
class NotesAdapter(private val onClick: (Note) -> Unit) :
    ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_note, parent, false), onClick
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NoteViewHolder(view: View, val onClick: (Note) -> Unit) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView = view.findViewById(R.id.noteTitle)
        private val contentText: TextView = view.findViewById(R.id.noteContent)
        private var current: Note? = null

        init { view.setOnClickListener { current?.let(onClick) } }

        fun bind(note: Note) {
            current = note
            titleText.text = note.title
            contentText.text = note.content
        }
    }
}

private class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
}
