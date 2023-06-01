package com.example.recipes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipes.domain.Repository
import com.example.recipes.presentation.recipe.RecipeArguments
import com.example.recipes.presentation.recipe.RecipeUiModel
import com.example.recipes.presentation.recipe.RecipeViewModel
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class RecipeViewModelUnitTests {
    @Mock
    private lateinit var repository: Repository
    private var recipeArguments = RecipeArguments(1, true)


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
    }

    private fun createViewModel() = RecipeViewModel(repository, recipeArguments)


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get recipe by id with data state test`() = runTest {
        Mockito.`when`(
            repository.getRecipeById(
                recipeArguments.id,
                recipeArguments.isFromApiSource
            )
        )
            .thenReturn(defaultRecipe)
        val viewModel = createViewModel()
        TestCase.assertEquals(RecipeUiModel.Data(defaultRecipe), viewModel.state.value)
    }

    @Test
    fun `get recipe by id with error state test`() = runTest {
        Mockito.`when`(
            repository.getRecipeById(
                recipeArguments.id,
                recipeArguments.isFromApiSource
            )
        )
            .thenThrow(RuntimeException())
        val viewModel = createViewModel()
        TestCase.assertEquals(RecipeUiModel.Error, viewModel.state.value)
    }

    @Test
    fun `save recipe with data state test`() = runTest {
        Mockito.`when`(
            repository.getRecipeById(
                recipeArguments.id,
                recipeArguments.isFromApiSource
            )
        )
            .thenReturn(defaultRecipe)
        Mockito.`when`(repository.saveRecipe(defaultRecipe)).thenReturn(100)
        val viewModel = createViewModel()
        viewModel.saveOrDeleteRecipe()
        verify(repository, times(1)).saveRecipe(defaultRecipe)
        verify(repository, times(0)).deleteRecipe(defaultRecipe)
        TestCase.assertEquals(
            RecipeUiModel.Data(defaultRecipe.copy(isSaved = true)),
            viewModel.state.value
        )
    }

    @Test
    fun `save recipe with error state test`() = runTest {
        Mockito.`when`(
            repository.getRecipeById(
                recipeArguments.id,
                recipeArguments.isFromApiSource
            )
        )
            .thenReturn(defaultRecipe)
        Mockito.`when`(repository.saveRecipe(defaultRecipe)).thenThrow(RuntimeException())
        val viewModel = createViewModel()
        viewModel.saveOrDeleteRecipe()
        verify(repository, times(1)).saveRecipe(defaultRecipe)
        verify(repository, times(0)).deleteRecipe(defaultRecipe)
        TestCase.assertEquals(R.string.save_error, viewModel.errorOfSave.value)
    }

    @Test
    fun `delete recipe with data state test`() = runTest {
        recipeArguments = RecipeArguments(1, false)
        val savedRecipe = defaultRecipe.copy(isSaved = true)
        Mockito.`when`(
            repository.getRecipeById(
                recipeArguments.id,
                recipeArguments.isFromApiSource
            )
        )
            .thenReturn(savedRecipe)
        val viewModel = createViewModel()
        viewModel.saveOrDeleteRecipe()
        verify(repository, times(1)).deleteRecipe(savedRecipe)
        verify(repository, times(0)).saveRecipe(savedRecipe)
        TestCase.assertEquals(
            RecipeUiModel.Data(defaultRecipe.copy(isSaved = false)),
            viewModel.state.value
        )
    }

    @Test
    fun `delete recipe with error state test`() = runTest {
        recipeArguments = RecipeArguments(1, false)
        val savedRecipe = defaultRecipe.copy(isSaved = true)
        Mockito.`when`(
            repository.getRecipeById(
                recipeArguments.id,
                recipeArguments.isFromApiSource
            )
        )
            .thenReturn(savedRecipe)
        Mockito.`when`(repository.deleteRecipe(savedRecipe)).thenThrow(RuntimeException())
        val viewModel = createViewModel()
        viewModel.saveOrDeleteRecipe()
        verify(repository, times(1)).deleteRecipe(savedRecipe)
        verify(repository, times(0)).saveRecipe(savedRecipe)
        TestCase.assertEquals(R.string.delete_error, viewModel.errorOfSave.value)
    }
}