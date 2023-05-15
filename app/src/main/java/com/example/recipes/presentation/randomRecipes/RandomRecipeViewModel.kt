package com.example.recipes.presentation.randomRecipes

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
class RandomRecipeViewModel (private val repository: Repository) : ViewModel() {

    private val _state = MutableLiveData<RandomRecipeUiModel>()
    val state: LiveData<RandomRecipeUiModel> = _state

    private val searchQuery = MutableStateFlow("")

    private val queryFlow = searchQuery.debounce(400L).distinctUntilChanged()

    init {
        getRecipe()
        collectQueryFlow()
    }

    fun setTitle(title: String) {
           searchQuery.value = title
    }

    private fun getRecipe() {
        viewModelScope.launch {
            _state.value = RandomRecipeUiModel.Loading
            try {
                _state.value = RandomRecipeUiModel.Data(
                    repository.getRandomRecipe()
                )
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "", e)
                _state.value = RandomRecipeUiModel.Error
            }
        }
    }

    private fun collectQueryFlow() {
        viewModelScope.launch {
            queryFlow.collectLatest {
                if(it.length > 2) {
                    _state.value = RandomRecipeUiModel.Loading
                    try {
                        _state.value = RandomRecipeUiModel.Data(
                            repository.getRecipeByRequest(it)
                        )
                    } catch (e: Exception) {
                        Log.e("RecipeViewModel", "", e)
                        _state.value = RandomRecipeUiModel.Error
                    }
                }else if(it.isEmpty()) {
                    getRecipe()
                }
            }

        }
    }


    class RandomRecipeFactory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RandomRecipeViewModel(repository) as T

        }
    }
}