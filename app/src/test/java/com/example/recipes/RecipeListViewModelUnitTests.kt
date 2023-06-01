package com.example.recipes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipes.domain.Repository
import com.example.recipes.presentation.recipeList.RecipeListUiModel
import com.example.recipes.presentation.recipeList.RecipeListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.mockito.Mockito.times
import org.mockito.Mockito.verify


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class RecipeListViewModelUnitTests {
    @Mock
    private lateinit var repository: Repository
    private var showOnlySaved: Boolean = false

    private val recipeList = listOf(defaultRecipe)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
    }

    private fun createViewModel() = RecipeListViewModel(repository, showOnlySaved)


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get recipe with data state test`() = runTest {
        Mockito.`when`(repository.getRandomRecipe(showOnlySaved)).thenReturn(recipeList)
        val viewModel = createViewModel()
        assertEquals(RecipeListUiModel.Data(recipeList), viewModel.state.value)
    }

    @Test
    fun `get recipe with error state test`() = runTest {
        Mockito.`when`(repository.getRandomRecipe(showOnlySaved)).thenThrow(RuntimeException())
        val viewModel = createViewModel()
        assertEquals(RecipeListUiModel.Error, viewModel.state.value)
    }

    @Test
    fun `collect query flow more then two characters test`() = runTest {
        val query = "test"
        Mockito.`when`(repository.getRandomRecipe(showOnlySaved)).thenReturn(recipeList)
        Mockito.`when`(repository.getRecipeByRequest(query, showOnlySaved))
            .thenReturn(recipeList)
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        verify(repository, times(1)).getRecipeByRequest(query, showOnlySaved)
        assertEquals(RecipeListUiModel.Data(recipeList), viewModel.state.value)
    }

    @Test
    fun `collect query flow less then two characters test`() = runTest {
        val query = "t"
        Mockito.`when`(repository.getRandomRecipe(showOnlySaved)).thenReturn(recipeList)
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        verify(repository, times(0)).getRecipeByRequest(query, showOnlySaved)
        assertEquals(RecipeListUiModel.Data(recipeList), viewModel.state.value)
    }

    @Test
    fun `collect query flow with empty query test`() = runTest {
        val query = ""
        Mockito.`when`(repository.getRandomRecipe(showOnlySaved)).thenReturn(recipeList)
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        verify(repository, times(0)).getRecipeByRequest(query, showOnlySaved)
        verify(repository, times(2)).getRandomRecipe(showOnlySaved)
    }

    @Test
    fun `collect query flow with error state`() = runTest {
        val query = "test"
        Mockito.`when`(repository.getRandomRecipe(showOnlySaved)).thenReturn(emptyList())
        Mockito.`when`(repository.getRecipeByRequest(query, showOnlySaved))
            .thenThrow(RuntimeException())
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        assertEquals(RecipeListUiModel.Error, viewModel.state.value)
    }
}
