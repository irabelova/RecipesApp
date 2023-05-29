package com.example.recipes.presentation.video

import android.util.Log
import androidx.lifecycle.*
import com.example.recipes.domain.Repository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class VideoViewModel(private val repository: Repository) : ViewModel() {

    private val _state = MutableLiveData<VideoUiModel>()
    val state: LiveData<VideoUiModel> = _state

    private val searchQuery = MutableStateFlow("")

    private val queryFlow = searchQuery.debounce(400L).distinctUntilChanged()

    init {
        collectQueryFlow()
    }

    fun setTitle(title: String) {
        searchQuery.value = title
    }

    private fun getVideoList(title: String) {
        viewModelScope.launch {
            _state.value = VideoUiModel.Loading
            try {
                _state.value = VideoUiModel.Data(
                    repository.getVideoList(title)
                )
            } catch (e: Exception) {
                Log.e("VideoViewModel", "", e)
                _state.value = VideoUiModel.Error
            }
        }
    }

    private fun collectQueryFlow() {
        viewModelScope.launch {
            queryFlow.collectLatest {
                if (it.length > 2) {
                    getVideoList(it)
                } else if (it.isEmpty()) {
                    getVideoList("Chicken")
                }
            }

        }
    }

    class VideoFactory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VideoViewModel(repository) as T

        }
    }
}