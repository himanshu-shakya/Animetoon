package com.animetoon.app.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.animetoon.app.R
import com.animetoon.app.databinding.FragmentDetailsBinding
import com.animetoon.app.ui.viewmdel.MainViewModel
import com.animetoon.app.utils.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the status bar color to light gray
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.LightGray)
        }

        // Observe selectedWebtoon state and display webtoon details
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.selectedWebtoon.collectLatest { webtoon ->
                webtoon?.let {
                    Log.i("TAG", "Selected Webtoon: $webtoon")
                    binding.imageView.load(it.imageUrl) {
                        crossfade(true)
                    }
                    binding.tvTitle.text = it.title
                    binding.tvCreator.text = it.creator
                    binding.tvReads.text = it.reads
                    binding.tvRating.text = it.averageRating.toString()
                    binding.tvDescription.text = it.detailedDescription
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isFavorite.collectLatest { isFavorite ->
                binding.btnFavorite.apply {
                    setImageResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite)


                    imageTintList = if (isFavorite) {
                        android.content.res.ColorStateList.valueOf(resources.getColor(R.color.orange))  // Favorite color
                    } else {
                        android.content.res.ColorStateList.valueOf(resources.getColor(android.R.color.white))  // Default white tint
                    }
                }
            }
        }


        binding.btnFavorite.setOnClickListener {
            mainViewModel.selectedWebtoon.value?.let { webtoon ->
                mainViewModel.toggleFavorite(webtoon)
            }
        }


        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailsFragment_to_homeFragment)
        }


        binding.btnSubmit.setOnClickListener {
            mainViewModel.selectedWebtoon.value?.let { webtoon ->
                mainViewModel.updateWebtoonRating(webtoon.id, binding.ratingBar.rating.toDouble(),requireContext())
            }
        }
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Enable the button only if the rating is greater than 0
            binding.btnSubmit.isEnabled = rating > 0
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.updateWebtoon.collectLatest { uiState ->
                when(uiState){
                    is UiState.Error -> {
                        Log.e("DetailsFragment", "Error updating webtoon rating: ${uiState.message}")
                    }
                    UiState.Idle -> {}
                    UiState.Loading -> {}
                    is UiState.Success -> {
                        Toast.makeText(requireContext(), "Thanks For Rating  \uD83D\uDE0A", Toast.LENGTH_SHORT).show()
                        binding.ratingBar.rating = 0f
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainViewModel.resetRattingState()
        // Change the status bar color back to orange when leaving the fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)
        }
    }
}
