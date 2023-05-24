package com.example.recipes.presentation.addAndEditRecipe

import androidx.lifecycle.*
import com.example.recipes.R
import com.example.recipes.domain.Repository
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import com.example.recipes.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class AddAndEditRecipeViewModel(private val repository: Repository) : ViewModel() {

    private val _title = MutableLiveData(ValidatableField(""))
    val title: LiveData<ValidatableField<String>> = _title

    private val _readyInMinutes = MutableLiveData(ValidatableField(""))
    val readyInMinutes: LiveData<ValidatableField<String>> = _readyInMinutes

    private val _extendedIngredients = MutableLiveData<ValidatableField<List<Ingredient>>>(ValidatableField(
        listOf()
    ))
    val extendedIngredients: LiveData<ValidatableField<List<Ingredient>>> = _extendedIngredients

    private val _instructions = MutableLiveData(ValidatableField(""))
    val instructions: LiveData<ValidatableField<String>> = _instructions

    private val _errorOfSave = SingleLiveEvent<Int>()
    val errorOfSave: LiveData<Int> = _errorOfSave

   private val _id = SingleLiveEvent<Int>()
    val id: LiveData<Int> = _id

    private var editingIngredientIndex: Int? = null


    fun setTitle(title: String) {
        if(_title.value?.value != title) {
            _title.value = ValidatableField(title)
        }
    }

    fun setReadyInMinutes(readyInMinutes: String) {
        if(_readyInMinutes.value?.value != readyInMinutes) {
            _readyInMinutes.value = ValidatableField(readyInMinutes)
        }
    }

    fun setInstructions(instructions: String) {
        if(_instructions.value?.value != instructions) {
            _instructions.value = ValidatableField(instructions)
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        val ingredients = extendedIngredients.value!!.value.toMutableList().apply {
            add(ingredient)
        }
        _extendedIngredients.value = ValidatableField(ingredients)
    }

    fun editIngredient(ingredient: Ingredient) {
        val ingredients = extendedIngredients.value!!.value.toMutableList()
        if(editingIngredientIndex != null) {
            ingredients[editingIngredientIndex!!] = ingredient
            _extendedIngredients.value = ValidatableField(ingredients)
        }
    }

    fun deleteIngredient(ingredient: Ingredient) {
        val ingredients = extendedIngredients.value!!.value.toMutableList().apply {
            remove(ingredient)
        }
        _extendedIngredients.value = ValidatableField(ingredients)
    }

    fun setEditingIngredient(ingredient: Ingredient) {
        val index = extendedIngredients.value!!.value.indexOf(ingredient)
        if(index >= 0) editingIngredientIndex = index
    }

    fun validateAndSaveRecipe() {
        var hasErrors = false
        val titleField = title.value ?: ValidatableField("")
        if(titleField.value.isEmpty()) {
            hasErrors = true
            _title.value = titleField.copy(showError = true)
        }
        val timeField = readyInMinutes.value ?: ValidatableField("")
        if(timeField.value.isEmpty()) {
            hasErrors = true
            _readyInMinutes.value = timeField.copy(showError = true)
        }
        val instructionField = instructions.value ?: ValidatableField("")
        if(instructionField.value.isEmpty()) {
            hasErrors = true
            _instructions.value = instructionField.copy(showError = true)
        }
        val ingredientsField = _extendedIngredients.value ?: ValidatableField(listOf())
        if(ingredientsField.value.isEmpty()) {
            hasErrors = true
            _extendedIngredients.value = ingredientsField.copy(showError = true)
        }

        if(!hasErrors) {
            val recipe = Recipe(
                id = 0,
                title = title.value!!.value,
                image = null,
                instructions = instructions.value!!.value,
                readyInMinutes = readyInMinutes.value!!.value.toInt(),
                extendedIngredients = extendedIngredients.value!!.value
            )
            saveRecipe(recipe)
        }
    }
    private fun saveRecipe(recipe: Recipe){
        viewModelScope.launch {
            try {
                val id = repository.saveRecipe(recipe)
                _id.value = id.toInt()
            } catch (e: Exception) {
                _errorOfSave.value = R.string.save_error
                e.printStackTrace()
            }
        }
    }


    class AddAndEditRecipeFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddAndEditRecipeViewModel(repository) as T
        }
    }
}