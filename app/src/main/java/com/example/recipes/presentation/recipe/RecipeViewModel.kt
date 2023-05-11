package com.example.recipes.presentation.recipe

import android.util.Log
import androidx.lifecycle.*
import com.example.recipes.domain.Repository
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RecipeViewModel (private val repository: Repository, private val id: Int): ViewModel() {

    private val _state = MutableLiveData<RecipeUiModel>()
    val state: LiveData<RecipeUiModel> = _state

    init {
        getRecipeById()
    }

    fun convertIngredients (ingredient: List<Ingredient>): String {
        var convertedText = ingredient.joinToString { element ->
            if (element.unit == "") {
                "- ${element.amount.roundToInt()} ${element.name}"
            }
            else {
            "- ${element.amount} ${element.unit} of ${element.name}"
            }
        }
        convertedText = convertedText.replace(", ", ",\n")
        return convertedText
    }

    fun convertInstructions (instructionText: Recipe): String {
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
    class RecipeFactory(private val repository: Repository, private val id: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeViewModel(repository, id) as T

        }
    }
}