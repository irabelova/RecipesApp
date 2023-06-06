package com.example.recipes.presentation.recipe

import android.util.Log
import androidx.lifecycle.*
import com.example.recipes.R
import com.example.recipes.domain.Repository
import com.example.recipes.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: Repository,
    private val recipeArguments: RecipeArguments) : ViewModel() {

    private val _state = MutableLiveData<RecipeUiModel>()
    val state: LiveData<RecipeUiModel> = _state

    private val _errorOfSave = SingleLiveEvent<Int>()
    val errorOfSave: LiveData<Int> = _errorOfSave

    private val _deleteEvent = SingleLiveEvent<Unit>()
    val deleteEvent: LiveData<Unit> = _deleteEvent

    private val _showAlertDialog = SingleLiveEvent<RecipeUiModel.Data>()
    val showAlertDialog: LiveData<RecipeUiModel.Data> = _showAlertDialog

    init {
        getRecipeById()
    }

    fun saveOrDeleteRecipe() {
        val data = state.value as? RecipeUiModel.Data
        data?.let {
            if (it.recipe.isSaved) {
                _showAlertDialog.value = it
            } else {
                saveRecipe(it)
            }
        }
    }


    fun deleteRecipe(data: RecipeUiModel.Data) {
        viewModelScope.launch {
            try {
                repository.deleteRecipe(data.recipe)
                _state.value = data.copy(recipe = data.recipe.copy(isSaved = false))
                _deleteEvent.call()
            } catch (e: Exception) {
                _errorOfSave.value = R.string.delete_error
                Log.e("RecipeViewModel", "Delete error", e)
            }
        }
    }

    private fun saveRecipe(data: RecipeUiModel.Data) {
        viewModelScope.launch {
            try {
                repository.saveRecipe(data.recipe)
                _state.value = data.copy(recipe = data.recipe.copy(isSaved = true))
            } catch (e: Exception) {
                _errorOfSave.value = R.string.save_error
                Log.e("RecipeViewModel", "Save error", e)
            }
        }
    }

    private fun getRecipeById() {
        viewModelScope.launch {
            _state.value = RecipeUiModel.Loading
            try {
                val recipe = repository.getRecipeById(recipeArguments.id, recipeArguments.isFromApiSource)
                _state.value = RecipeUiModel.Data(recipe)

            } catch (e: Exception) {
                Log.e("RecipeViewModel", "", e)
                _state.value = RecipeUiModel.Error
            }
        }
    }

    class RecipeFactory(
        private val repository: Repository,
        private val recipeArguments: RecipeArguments):
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeViewModel(repository, recipeArguments) as T

        }
    }
}