package com.example.recipes.presentation.recipe

import android.util.Log
import androidx.lifecycle.*
import com.example.recipes.R
import com.example.recipes.domain.Repository
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import com.example.recipes.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RecipeViewModel(private val repository: Repository, private val id: Int) : ViewModel() {

    private val _state = MutableLiveData<RecipeUiModel>()
    val state: LiveData<RecipeUiModel> = _state

    private val _errorOfSave = SingleLiveEvent<Int>()
    val errorOfSave: LiveData<Int> = _errorOfSave

    private val _deleteEvent = SingleLiveEvent<Unit>()
    val deleteEvent: LiveData<Unit> = _deleteEvent

    init {
        getRecipeById()
    }

    fun convertIngredients(ingredient: List<Ingredient>): String {
        var convertedText = ingredient.joinToString { element ->
            if (element.unit == "") {
                "- ${element.amount.roundToInt()} ${element.name}"
            } else {
                "- ${element.amount} ${element.unit} of ${element.name}"
            }
        }
        convertedText = convertedText.replace(", ", ",\n")
        return convertedText
    }

    fun convertInstructions(instructionText: Recipe): String {
        val oldStr = listOf("<ol><li>", "</li></ol>", "</li><li>")
        var convertedText = instructionText.instructions
        for (element in oldStr) {
            if (convertedText.contains(element)) {
                convertedText = convertedText.replace(
                    element, ""
                )
            }
        }
        convertedText = convertedText.replace(". ", ".\n")
        return convertedText
    }

    fun saveOrDeleteRecipe() {
        val data = state.value as? RecipeUiModel.Data
        data?.let {
            if (it.recipe.isSaved) {
                deleteRecipe(it)
            } else {
                saveRecipe(it)
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
            }
        }
    }

    private fun deleteRecipe(data: RecipeUiModel.Data) {
        viewModelScope.launch {
            try {
                repository.deleteRecipe(data.recipe)
                _state.value = data.copy(recipe = data.recipe.copy(isSaved = false))
                _deleteEvent.call()
            } catch (e: Exception) {
                _errorOfSave.value = R.string.delete_error
            }
        }
    }

    private fun getRecipeById() {
        viewModelScope.launch {
            _state.value = RecipeUiModel.Loading
            try {
                val recipe = repository.getRecipeById(id)
                _state.value = RecipeUiModel.Data(recipe)

            } catch (e: Exception) {
                Log.e("RecipeViewModel", "", e)
                _state.value = RecipeUiModel.Error
            }
        }
    }

    class RecipeFactory(private val repository: Repository, private val id: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeViewModel(repository, id) as T

        }
    }
}