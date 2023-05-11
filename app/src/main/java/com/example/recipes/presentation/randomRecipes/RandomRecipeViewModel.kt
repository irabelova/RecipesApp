package com.example.recipes.presentation.randomRecipes

import android.util.Log
import androidx.lifecycle.*

import com.example.recipes.domain.Repository
import kotlinx.coroutines.launch


class RandomRecipeViewModel (private val repository: Repository) : ViewModel() {

    private val _state = MutableLiveData<RandomRecipeUiModel>()
    val state: LiveData<RandomRecipeUiModel> = _state

    init {
        getRecipe()
    }

    private fun getRecipe() {
        viewModelScope.launch {
            _state.value = RandomRecipeUiModel.Loading
            try {
                _state.value = RandomRecipeUiModel.Data(repository.getRandomRecipe())
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "", e)
                _state.value = RandomRecipeUiModel.Error
            }
        }
    }

    class RandomRecipeFactory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RandomRecipeViewModel(repository) as T

        }
    }
}