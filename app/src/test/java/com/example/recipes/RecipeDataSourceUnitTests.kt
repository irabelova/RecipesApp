package com.example.recipes

import com.example.recipes.data.database.RecipeDao
import com.example.recipes.domain.RecipeDataSource
import com.example.recipes.domain.mappers.RecipeDbMapper
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class RecipeDataSourceUnitTests {

    @Mock
    private lateinit var recipeDao: RecipeDao

    @Mock
    private lateinit var recipeDbMapper: RecipeDbMapper


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    private fun createRecipeDataSource() = RecipeDataSource(recipeDao, recipeDbMapper)


    @Test
    fun `save recipe test`() = runTest {
        Mockito.`when`(recipeDao.insertIngredient(ingredientDb))
            .thenReturn(ingredientDb.ingredientsId.toLong())
        Mockito.`when`(recipeDao.insertRecipe(recipeDb)).thenReturn(recipeDb.recipeId.toLong())
        Mockito.`when`(recipeDbMapper.recipeToRecipeWithIngredients(defaultRecipe))
            .thenReturn(recipeWithIngredients)
        val recipeDataSource = createRecipeDataSource()
        val result = recipeDataSource.saveRecipe(defaultRecipe)
        verify(recipeDao, times(1)).insertIngredient(ingredientDb)
        verify(recipeDao, times(1)).insertRecipe(recipeDb)
        verify(recipeDbMapper, times(1)).recipeToRecipeWithIngredients(defaultRecipe)
        TestCase.assertEquals(recipeDb.recipeId.toLong(), result)
    }

    @Test
    fun `delete recipe test`() = runTest {
        Mockito.`when`(recipeDbMapper.recipeToRecipeWithIngredients(defaultRecipe))
            .thenReturn(recipeWithIngredients)
        val recipeDataSource = createRecipeDataSource()
        recipeDataSource.deleteRecipe(defaultRecipe)
        verify(recipeDao, times(1)).deleteRecipeWithIngredients(recipeWithIngredients)
        verify(recipeDbMapper, times(1)).recipeToRecipeWithIngredients(defaultRecipe)
    }

    @Test
    fun `get random recipe test`() = runTest {
        val savedRecipe = defaultRecipe.copy(isSaved = true)
        Mockito.`when`(recipeDao.getRecipesWithIngredients())
            .thenReturn(listOf(recipeWithIngredients))
        Mockito.`when`(recipeDbMapper.recipeWithIngredientsToRecipe(recipeWithIngredients))
            .thenReturn(
                savedRecipe
            )
        val recipeDataSource = createRecipeDataSource()
        val result = recipeDataSource.getRandomRecipe()
        verify(recipeDao, times(1)).getRecipesWithIngredients()
        verify(recipeDbMapper, times(1)).recipeWithIngredientsToRecipe(recipeWithIngredients)
        TestCase.assertEquals(listOf(savedRecipe), result)
    }

    @Test
    fun `get recipe by id test`() = runTest {
        val savedRecipe = defaultRecipe.copy(isSaved = true)
        Mockito.`when`(recipeDao.getRecipeById(defaultRecipe.id)).thenReturn(recipeWithIngredients)
        Mockito.`when`(recipeDbMapper.recipeWithIngredientsToRecipe(recipeWithIngredients))
            .thenReturn(
                savedRecipe
            )
        val recipeDataSource = createRecipeDataSource()
        val result = recipeDataSource.getRecipeById(defaultRecipe.id)
        verify(recipeDao, times(1)).getRecipeById(defaultRecipe.id)
        verify(recipeDbMapper, times(1)).recipeWithIngredientsToRecipe(recipeWithIngredients)
        TestCase.assertEquals(savedRecipe, result)
    }

    @Test
    fun `get recipe by request test`() = runTest {
        val savedRecipe = defaultRecipe.copy(isSaved = true)
        Mockito.`when`(recipeDao.getRecipeByRequest(defaultRecipe.title))
            .thenReturn(listOf(recipeWithIngredients))
        Mockito.`when`(recipeDbMapper.recipeWithIngredientsToRecipe(recipeWithIngredients))
            .thenReturn(
                savedRecipe
            )
        val recipeDataSource = createRecipeDataSource()
        val result = recipeDataSource.getRecipeByRequest(defaultRecipe.title)
        verify(recipeDao, times(1)).getRecipeByRequest(defaultRecipe.title)
        verify(recipeDbMapper, times(1)).recipeWithIngredientsToRecipe(recipeWithIngredients)
        TestCase.assertEquals(listOf(savedRecipe), result)
    }

    @Test
    fun `update recipe test`() = runTest {
        Mockito.`when`(recipeDbMapper.recipeToRecipeWithIngredients(defaultRecipe))
            .thenReturn(recipeWithIngredients)
        val recipeDataSource = createRecipeDataSource()
        recipeDataSource.updateRecipe(defaultRecipe)
        verify(recipeDao, times(1)).updateRecipeWithIngredients(recipeWithIngredients)
        verify(recipeDbMapper, times(1)).recipeToRecipeWithIngredients(defaultRecipe)
    }
}