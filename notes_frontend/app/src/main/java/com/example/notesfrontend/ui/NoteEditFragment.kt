package com.example.notesfrontend.ui

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notesfrontend.R
import com.example.notesfrontend.data.Note
import com.example.notesfrontend.databinding.FragmentNoteEditBinding

/**
 * PUBLIC_INTERFACE
 * Fragment for creating or editing a note.
 */
class NoteEditFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SingleNoteViewModel
    private var noteId: Long = -1L
    private var editingNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = it.getLong("noteId", -1L)
        }
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(SingleNoteViewModel::class.java)
        if (noteId != -1L) {
            viewModel.loadNote(noteId)
        }
        viewModel.note.observe(viewLifecycleOwner) { note ->
            editingNote = note
            note?.let {
                binding.titleEditText.setText(it.title)
                binding.contentEditText.setText(it.content)
            }
        }
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text?.toString()?.trim() ?: ""
            val content = binding.contentEditText.text?.toString()?.trim() ?: ""
            if (title.isEmpty() && content.isEmpty()) {
                binding.titleEditText.error = getString(R.string.title_hint)
                return@setOnClickListener
            }
            if (editingNote == null) {
                // create new
                viewModel.insert(Note(title = title, content = content)) {
                    findNavController().popBackStack()
                }
            } else {
                // update existing
                editingNote?.apply {
                    this.title = title
                    this.content = content
                }?.let { updated ->
                    viewModel.update(updated) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
        binding.cancelButton.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
