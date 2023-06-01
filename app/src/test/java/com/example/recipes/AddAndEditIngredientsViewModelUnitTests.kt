package com.example.recipes


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsViewModel
import com.example.recipes.presentation.addAndEditRecipe.ValidatableField
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test


internal class AddAndEditIngredientsViewModelUnitTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private fun createViewModel(ingredient: Ingredient? = null) = AddAndEditIngredientsViewModel(ingredient)

    @Test
    fun `set amount test`() {
        val amount = "2.0"
        val viewModel = createViewModel()
        viewModel.setAmount(amount)
        TestCase.assertEquals(amount, viewModel.amount.value?.value)
    }

    @Test
    fun `set unit test`() {
        val unit = "pounds"
        val viewModel = createViewModel()
        viewModel.setUnit(unit)
        TestCase.assertEquals(unit, viewModel.unit.value?.value)
    }

    @Test
    fun `set name test`() {
        val name = "oranges"
        val viewModel = createViewModel()
        viewModel.setName(name)
        TestCase.assertEquals(name, viewModel.name.value?.value)
    }

    @Test
    fun `validate and save ingredient with empty ingredient fields test`() {
        val viewModel = createViewModel()
        viewModel.validateAndSaveIngredients()
        TestCase.assertEquals(ValidatableField("").copy(showError = true), viewModel.amount.value)
        TestCase.assertEquals(
            ValidatableField("").copy(showError = true), viewModel.name.value
        )
    }

    @Test
    fun `validate and save added ingredient test` () {
        val viewModel = createViewModel()
        viewModel.setAmount("2.0")
        viewModel.setName("apples")
        viewModel.validateAndSaveIngredients()
        TestCase.assertEquals(Ingredient(id = 0, amount = 2.0, unit = "", name = "apples"), viewModel.addedIngredient.value)
    }

    @Test
    fun `validate and save edited ingredient test` () {
        val viewModel = createViewModel(defaultIngredient)
        viewModel.validateAndSaveIngredients()
        TestCase.assertEquals(defaultIngredient, viewModel.editedIngredient.value)
    }
}