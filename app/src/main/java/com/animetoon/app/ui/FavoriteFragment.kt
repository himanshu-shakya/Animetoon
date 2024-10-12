package com.animetoon.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.animetoon.app.R
import com.animetoon.app.data.adapter.WebtoonListAdapter
import com.animetoon.app.databinding.FragmentFavoriteBinding
import com.animetoon.app.ui.viewmdel.MainViewModel
import com.animetoon.app.utils.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteFragment : Fragment() {
    private lateinit var favoriteListAdapter: WebtoonListAdapter
    private val mainViewModel by activityViewModel<MainViewModel>()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoriteListAdapter = WebtoonListAdapter(
            webtoonList = emptyList(),
            onItemClick = { webtoon ->
                Log.i("TAG", "onViewCreated: Home $webtoon")
                mainViewModel.selectedWebtoonChanged(webtoon)
                findNavController().navigate(R.id.action_favoriteFragment_to_detailsFragment)
            },
            onRemoveFavoriteClick = { webtoon ->
                mainViewModel.toggleFavorite(webtoon)
                mainViewModel.getFavorites()
            },
            isFavoriteScreen = true
        )

        binding.recyclerView.adapter = favoriteListAdapter


        mainViewModel.getFavorites()

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.getFavorites.collect { favorites ->
                when (favorites) {
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), favorites.message, Toast.LENGTH_SHORT).show()
                        binding.loadingAnimationView.cancelAnimation()
                        binding.loadingLayout.visibility = View.GONE
                        binding.loadingAnimationView.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyFavoritesLayout.visibility = View.VISIBLE // Show no favorites message
                    }

                    UiState.Loading -> {
                        binding.loadingAnimationView.visibility = View.VISIBLE
                        binding.loadingAnimationView.playAnimation()
                        binding.loadingLayout.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyFavoritesLayout.visibility = View.GONE // Hide no favorites message
                    }

                    is UiState.Success -> {
                        if (favorites.data.isEmpty()) {
                            binding.emptyFavoritesLayout.visibility = View.VISIBLE // Show no favorites message
                            binding.recyclerView.visibility = View.GONE // Hide RecyclerView
                            binding.loadingAnimationView.visibility = View.GONE
                            binding.loadingLayout.visibility = View.GONE
                        } else {
                            favoriteListAdapter.updateData(favorites.data)
                            binding.loadingAnimationView.cancelAnimation()
                            binding.loadingAnimationView.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE // Show RecyclerView
                            binding.emptyFavoritesLayout.visibility = View.GONE // Hide no favorites message
                            binding.loadingLayout.visibility = View.GONE
                        }
                    }

                    else -> {
                        binding.loadingLayout.visibility = View.GONE
                        binding.loadingAnimationView.cancelAnimation()
                        binding.loadingAnimationView.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyFavoritesLayout.visibility = View.VISIBLE // Show no favorites message
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment) // Navigate to HomeFragment
            findNavController().popBackStack(R.id.favoriteFragment, true) // Ensure FavoriteFragment is removed from back stack
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
