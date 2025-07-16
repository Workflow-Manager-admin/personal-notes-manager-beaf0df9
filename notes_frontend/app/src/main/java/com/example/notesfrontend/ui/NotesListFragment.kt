package com.example.notesfrontend.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesfrontend.R
import com.example.notesfrontend.data.Note
import com.example.notesfrontend.databinding.FragmentNotesListBinding

/**
 * PUBLIC_INTERFACE
 * Fragment displaying list of notes, top bar, search, and FAB to add note.
 */
class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(NoteViewModel::class.java)
        adapter = NotesAdapter { note ->
            // Navigate to detail
            val action = NotesListFragmentDirections.actionNotesListFragmentToNoteDetailFragment(note.id)
            findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        
        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(notes)
            binding.emptyText.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.search(text?.toString() ?: "")
        }

        binding.addNoteFab.setOnClickListener {
            val action = NotesListFragmentDirections.actionNotesListFragmentToNoteEditFragment(-1)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
