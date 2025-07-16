package com.example.notesfrontend.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notesfrontend.R
import com.example.notesfrontend.data.Note
import com.example.notesfrontend.databinding.FragmentNoteDetailBinding
import java.text.DateFormat

/**
 * PUBLIC_INTERFACE
 * Fragment showing details of one note, and edit/delete actions.
 */
class NoteDetailFragment : Fragment() {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SingleNoteViewModel
    private var noteId: Long = -1L
    private var currentNote: Note? = null

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
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(SingleNoteViewModel::class.java)
        viewModel.loadNote(noteId)
        viewModel.note.observe(viewLifecycleOwner) { note ->
            currentNote = note
            note?.let { n ->
                binding.titleTextView.text = n.title
                binding.contentTextView.text = n.content
                binding.createdTextView.text = getString(R.string.lbl_created) + " " +
                        DateFormat.getDateTimeInstance().format(n.created)
                binding.updatedTextView.text = getString(R.string.lbl_updated) + " " +
                        DateFormat.getDateTimeInstance().format(n.updated)
            }
        }
        binding.editButton.setOnClickListener {
            val action = NoteDetailFragmentDirections.actionNoteDetailFragmentToNoteEditFragment(noteId)
            findNavController().navigate(action)
        }
        binding.deleteButton.setOnClickListener {
            currentNote?.let { n ->
                viewModel.delete(n) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
