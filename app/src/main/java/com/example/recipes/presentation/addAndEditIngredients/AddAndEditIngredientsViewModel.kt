package com.example.recipes.presentation.addAndEditIngredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.presentation.addAndEditRecipe.ValidatableField
import com.example.recipes.utils.SingleLiveEvent

class AddAndEditIngredientsViewModel(private val ingredient: Ingredient?) : ViewModel() {

    private val _amount = MutableLiveData(ValidatableField(""))
    val amount: LiveData<ValidatableField<String>> = _amount

    private val _unit = MutableLiveData(ValidatableField(""))
    val unit: LiveData<ValidatableField<String>> = _unit

    private val _name = MutableLiveData(ValidatableField(""))
    val name: LiveData<ValidatableField<String>> = _name

    private val _addedIngredient = SingleLiveEvent<Ingredient>()
    val addedIngredient: LiveData<Ingredient> = _addedIngredient

    private val _editedIngredient = SingleLiveEvent<Ingredient>()
    val editedIngredient: LiveData<Ingredient> = _editedIngredient

    init {
        setIngredient(ingredient)
    }

    fun setAmount(amount: String) {
        if (_amount.value?.value != amount) {
            _amount.value = ValidatableField(amount)
        }
    }

    fun setUnit(unit: String) {
        if (_unit.value?.value != unit) {
            _unit.value = ValidatableField(unit)
        }
    }

    fun setName(name: String) {
        if (_name.value?.value != name) {
            _name.value = ValidatableField(name)
        }
    }

    fun validateAndSaveIngredients() {
        var hasErrors = false
        val amountField = amount.value ?: ValidatableField("")
        if (amountField.value.isEmpty()) {
            hasErrors = true
            _amount.value = amountField.copy(showError = true)
        }
        val nameField = name.value ?: ValidatableField("")
        if (nameField.value.isEmpty()) {
            hasErrors = true
            _name.value = nameField.copy(showError = true)
        }
        if (!hasErrors) {
            val createdIngredient = createIngredient()
            if (ingredient == null) {
                _addedIngredient.value = createdIngredient
            } else {
                _editedIngredient.value = createdIngredient
            }
        }

    }

    private fun createIngredient(): Ingredient {
        return Ingredient(
            id = ingredient?.id ?: 0,
            amount = amount.value!!.value.toDouble(),
            unit = unit.value!!.value,
            name = name.value!!.value
        )
    }


    private fun setIngredient(ingredient: Ingredient?) {
        if (ingredient != null) {
            setAmount(ingredient.amount.toString())
            setName(ingredient.name)
            setUnit(ingredient.unit)
        }
    }

    class AddAndEditIngredientsFactory(private val ingredient: Ingredient?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddAndEditIngredientsViewModel(ingredient) as T
        }
    }
}