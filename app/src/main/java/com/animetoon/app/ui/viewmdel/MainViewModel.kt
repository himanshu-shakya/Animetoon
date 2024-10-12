package com.animetoon.app.ui.viewmdel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animetoon.app.data.db.dao.WebtoonDao
import com.animetoon.app.data.model.FavoriteWebtoon
import com.animetoon.app.data.model.Webtoon
import com.animetoon.app.data.model.toFavoriteWebtoon
import com.animetoon.app.data.model.toWebtoon
import com.animetoon.app.data.repository.MainRepository
import com.animetoon.app.utils.Result
import com.animetoon.app.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository,
    private val webtoonDao: WebtoonDao
) : ViewModel() {

    private val _getWebtoon = MutableStateFlow<UiState<List<Webtoon>>>(UiState.Idle)
    val getWebtoon = _getWebtoon.asStateFlow()

    private val _selectedWebtoon = MutableStateFlow<Webtoon?>(null)
    val selectedWebtoon = _selectedWebtoon.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _getFavorites = MutableStateFlow<UiState<List<Webtoon>>>(UiState.Idle)
    val getFavorites = _getFavorites.asStateFlow()

    private val _updateWebtoonRating = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val updateWebtoon = _updateWebtoonRating.asStateFlow()

    fun getWebtoons() {
        _getWebtoon.update { UiState.Loading }
        viewModelScope.launch {
            when (val result = repository.getWebtoonsFromJson()) {
                is Result.Error -> {
                    _getWebtoon.update { UiState.Error(result.error) }
                }

                is Result.Success -> {
                    _getWebtoon.update { UiState.Success(result.data) }
                }
            }
        }
    }

    fun selectedWebtoonChanged(webtoon: Webtoon) {
        Log.d("MainViewModel", "Selected Webtoon: $webtoon")
        _selectedWebtoon.update { webtoon }
        checkIfFavorite(webtoon.id)
    }

    private fun checkIfFavorite(webtoonId: Int) {
        viewModelScope.launch {
            val favoriteExists = webtoonDao.isFavorite(webtoonId)
            _isFavorite.update { favoriteExists != null }
        }
    }

    fun toggleFavorite(webtoon: Webtoon) {
        if (_isFavorite.value) {
            removeFromFavorites(webtoon)
        } else {
            addToFavorites(webtoon)
        }
    }

    private fun addToFavorites(webtoon: Webtoon) {
        viewModelScope.launch {
            webtoonDao.addFavorite(webtoon.toFavoriteWebtoon())
            _isFavorite.update { true }
        }
    }

    private fun removeFromFavorites(webtoon: Webtoon) {
        viewModelScope.launch {
            webtoonDao.removeFavorite(webtoon.toFavoriteWebtoon())
            _isFavorite.update { false }
        }
    }

    fun getFavorites() {
        Log.d("MainViewModel", "Fetching favorites...")
        _getFavorites.update { UiState.Loading }
        viewModelScope.launch {
            delay(1000) // Simulate loading
            val favorites = webtoonDao.getAllFavorites()
            val webtoons = convertFavoritesToWebtoons(favorites)
            _getFavorites.update { UiState.Success(webtoons) }
        }
    }

    private fun convertFavoritesToWebtoons(favoriteWebtoons: List<FavoriteWebtoon>): List<Webtoon> {
        return favoriteWebtoons.map { it.toWebtoon() }
    }

    fun updateWebtoonRating(webtoonId: Int, newRating: Double,context: Context) {
        viewModelScope.launch {
            when (val result = repository.getWebtoonsFromJson()) {
                is Result.Success -> updateWebtoon(result.data, webtoonId, newRating,context)
                is Result.Error -> Log.e(
                    "MainViewModel",
                    "Error fetching webtoons: ${result.error}"
                )
            }
        }
    }

    private fun updateWebtoon(webtoons: List<Webtoon>, webtoonId: Int, newRating: Double,context:Context) {
        val webtoon = webtoons.find { it.id == webtoonId }
        webtoon?.let {
            val updatedWebtoon = it.updateRating(newRating)
            viewModelScope.launch {
                when (val result = repository.updateJsonData(updatedWebtoon = updatedWebtoon, context = context)) {
                    is Result.Error -> {
                        _updateWebtoonRating.update { UiState.Error(result.error) }
                    }

                    is Result.Success -> {
                        _updateWebtoonRating.update { UiState.Success(result.data) }
                        // Reload the selected webtoon after updating the rating
                        selectedWebtoonChanged(updatedWebtoon)
                    }
                }
            }
        }
    }

    private fun Webtoon.updateRating(newRating: Double): Webtoon {
        val totalRatings = this.totalRatings + 1
        val totalScore = this.totalScore + newRating
        val averageRating = totalScore / totalRatings

        return this.copy(
            averageRating = averageRating,
            totalRatings = totalRatings,
            totalScore = totalScore.toInt()
        )
    }
    fun resetRattingState(){
        _updateWebtoonRating.update { UiState.Idle }
    }
}
