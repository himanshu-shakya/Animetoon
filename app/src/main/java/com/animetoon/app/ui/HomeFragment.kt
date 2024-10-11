package com.animetoon.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.animetoon.app.R
import com.animetoon.app.data.adapter.WebtoonListAdapter
import com.animetoon.app.data.model.Webtoon
import com.animetoon.app.databinding.FragmentHomeBinding
import com.animetoon.app.ui.viewmdel.MainViewModel
import com.animetoon.app.utils.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<MainViewModel>()
    private lateinit var webtoonListAdapter: WebtoonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        webtoonListAdapter = WebtoonListAdapter(emptyList()) { webtoon ->
            // Handle item click here
        }
        binding.recyclerView.adapter = webtoonListAdapter // Set the adapter

        // Call the ViewModel function to load data
        mainViewModel.getWebtoons()

        // Collect the state from the ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.getWebtoon.collectLatest { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        // Show loading indicator (if you have one)
                        // e.g., binding.loadingIndicator.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        // Update the adapter with the retrieved webtoons
                        webtoonListAdapter.updateData(uiState.data) // Make sure to implement this method in your adapter
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Handle idle state if needed
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding when the view is destroyed
    }
}
