package com.example.recipes

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipes.domain.Repository
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import com.example.recipes.presentation.addAndEditRecipe.AddAndEditRecipeViewModel
import com.example.recipes.presentation.addAndEditRecipe.ValidatableField
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class AddAndEditRecipeViewModelUnitTests {
    @Mock
    private lateinit var repository: Repository
    @Mock
    private lateinit var uri: Uri


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
    }

    private fun createViewModel(recipe: Recipe? = null) = AddAndEditRecipeViewModel(repository, recipe)


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `set title test`() {
        val query = "test"
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        TestCase.assertEquals(query, viewModel.title.value?.value)
    }

    @Test
    fun `set ready in minutes test`() {
        val query = "test"
        val viewModel = createViewModel()
        viewModel.setReadyInMinutes(query)
        TestCase.assertEquals(query, viewModel.readyInMinutes.value?.value)
    }

    @Test
    fun `set instructions test`() {
        val query = "test"
        val viewModel = createViewModel()
        viewModel.setInstructions(query)
        TestCase.assertEquals(query, viewModel.instructions.value?.value)
    }

    @Test
    fun `add ingredient test`() {
        val viewModel = createViewModel()
        viewModel.addIngredient(defaultIngredient)
        TestCase.assertEquals(listOf(defaultIngredient), viewModel.extendedIngredients.value?.value)
    }

    @Test
    fun `edit ingredient test`() {
        val viewModel = createViewModel()
        viewModel.addIngredient(defaultIngredient)
        TestCase.assertEquals(listOf(defaultIngredient), viewModel.extendedIngredients.value?.value)
        viewModel.setEditingIngredient(defaultIngredient)
        viewModel.editIngredient(defaultIngredient)
        TestCase.assertEquals(listOf(defaultIngredient), viewModel.extendedIngredients.value?.value)
    }

    @Test
    fun `edit non-existent ingredient test`() {
        val viewModel = createViewModel()
        viewModel.setEditingIngredient(defaultIngredient)
        viewModel.editIngredient(defaultIngredient)
        TestCase.assertEquals(emptyList<Ingredient>(), viewModel.extendedIngredients.value?.value)
    }

    @Test
    fun `set uri test`() {
        val str = "test"
        val viewModel = createViewModel()
        Mockito.`when`(uri.toString()).thenReturn(str)
        viewModel.setUri(uri)
        TestCase.assertEquals(str, viewModel.uriString.value)
    }

    @Test
    fun `delete ingredient test`() {
        val viewModel = createViewModel()
        viewModel.addIngredient(defaultIngredient)
        TestCase.assertEquals(listOf(defaultIngredient), viewModel.extendedIngredients.value?.value)
        viewModel.deleteIngredient(defaultIngredient)
        TestCase.assertEquals(emptyList<Ingredient>(), viewModel.extendedIngredients.value?.value)
    }

    @Test
    fun `validate and save recipe with empty recipe fields test`() = runTest {
        val viewModel = createViewModel()
        viewModel.validateAndSaveRecipe()
        TestCase.assertEquals(ValidatableField("").copy(showError = true), viewModel.title.value)
        TestCase.assertEquals(
            ValidatableField("").copy(showError = true), viewModel.instructions.value
        )
        TestCase.assertEquals(
            ValidatableField("").copy(showError = true), viewModel.readyInMinutes.value
        )
        TestCase.assertEquals(
            ValidatableField(listOf<Ingredient>()).copy(showError = true),
            viewModel.extendedIngredients.value
        )
    }

    @Test
    fun `validate and save recipe test`() = runTest {
        Mockito.`when`(repository.saveRecipe(defaultRecipe)).thenReturn(defaultRecipe.id.toLong())
        val viewModel = createViewModel()
        viewModel.setTitle(defaultRecipe.title)
        viewModel.setReadyInMinutes(defaultRecipe.readyInMinutes.toString())
        viewModel.setInstructions(defaultRecipe.instructions)
        viewModel.addIngredient(defaultIngredient)
        viewModel.validateAndSaveRecipe()
        TestCase.assertEquals(0, viewModel.id.value)
    }

    @Test
    fun `validate and save recipe with error test`() = runTest {
        Mockito.`when`(repository.saveRecipe(defaultRecipe)).thenThrow(RuntimeException())
        val viewModel = createViewModel()
        viewModel.setTitle(defaultRecipe.title)
        viewModel.setReadyInMinutes(defaultRecipe.readyInMinutes.toString())
        viewModel.setInstructions(defaultRecipe.instructions)
        viewModel.addIngredient(defaultIngredient)
        viewModel.validateAndSaveRecipe()
        TestCase.assertEquals(R.string.save_error, viewModel.errorOfSave.value)
    }

    @Test
    fun `validate and update recipe test`() = runTest {
        val viewModel = createViewModel(defaultRecipe)
        viewModel.validateAndSaveRecipe()
        TestCase.assertEquals(0, viewModel.id.value)
    }

    @Test
    fun `validate and update recipe with error test`() = runTest {
        Mockito.`when`(repository.updateRecipe(defaultRecipe)).thenThrow(RuntimeException())
        val viewModel = createViewModel(defaultRecipe)
        viewModel.validateAndSaveRecipe()
        TestCase.assertEquals(R.string.save_error, viewModel.errorOfSave.value)
    }

}