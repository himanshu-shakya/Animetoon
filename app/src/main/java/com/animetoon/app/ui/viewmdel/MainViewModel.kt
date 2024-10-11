package com.animetoon.app.ui.viewmdel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animetoon.app.data.model.Webtoon
import com.animetoon.app.data.repository.MainRepository
import com.animetoon.app.utils.Result
import com.animetoon.app.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel (private val repository: MainRepository): ViewModel() {
    private val _getWebtoon = MutableStateFlow<UiState<List<Webtoon>>>(UiState.Idle)
    val getWebtoon = _getWebtoon.asStateFlow()

    fun getWebtoons() {
        _getWebtoon.update { UiState.Loading }
        viewModelScope.launch {
            when(val result = repository.getWebtoonsFromJson()){
                is Result.Error -> {
                    _getWebtoon.update { UiState.Error(result.error)  }
                }
                is Result.Success -> {
                    _getWebtoon.update { UiState.Success(result.data)  }
                }
            }
        }

    }

}