package com.animetoon.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.animetoon.app.R
import com.animetoon.app.data.adapter.WebtoonListAdapter
import com.animetoon.app.data.model.Webtoon
import com.animetoon.app.databinding.FragmentHomeBinding
import com.animetoon.app.ui.viewmdel.MainViewModel
import com.animetoon.app.utils.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModel<MainViewModel>()
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
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        webtoonListAdapter = WebtoonListAdapter(
            webtoonList = emptyList(),
            onItemClick = { webtoon ->
                Log.i("TAG", "onViewCreated: Home $webtoon")
                mainViewModel.selectedWebtoonChanged(webtoon)
                findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
            }
        )
        binding.recyclerView.adapter = webtoonListAdapter // Set the adapter

        mainViewModel.getWebtoons()
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.getWebtoon.collectLatest { uiState ->
                when (uiState) {
                    is UiState.Loading -> {

                        binding.loadingAnimationView.visibility = View.VISIBLE
                        binding.loadingLayout.visibility = View.VISIBLE
                        binding.loadingAnimationView.playAnimation()
                    }

                    is UiState.Success -> {

                        binding.loadingAnimationView.cancelAnimation()
                        binding.loadingAnimationView.visibility = View.GONE
                        binding.loadingLayout.visibility = View.GONE
                        webtoonListAdapter.updateData(uiState.data)
                    }

                    is UiState.Error -> {

                        binding.loadingAnimationView.cancelAnimation()
                        binding.loadingAnimationView.visibility = View.GONE
                        binding.loadingLayout.visibility = View.GONE

                        // Show an error message
                        Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        // Handle other cases if needed (e.g., idle state)
                        binding.loadingAnimationView.cancelAnimation()
                        binding.loadingLayout.visibility = View.GONE
                        binding.loadingAnimationView.visibility = View.GONE
                    }
                }
            }
        }


        binding.favoriteButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
