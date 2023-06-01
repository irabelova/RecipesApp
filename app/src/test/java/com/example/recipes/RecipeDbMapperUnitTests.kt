package com.example.recipes

import com.example.recipes.data.database.RecipeWithIngredients
import com.example.recipes.domain.mappers.RecipeDbMapper
import junit.framework.TestCase
import org.junit.Test

internal class RecipeDbMapperUnitTests {

    private val ingredientDbList = listOf(ingredientDb)

    private val recipeWithIngredients = RecipeWithIngredients (recipeDb, ingredientDbList)

    @Test
    fun `recipe with ingredients to recipe test` () {
        val recipeDbMapper = RecipeDbMapper()
        val result = recipeDbMapper.recipeWithIngredientsToRecipe(recipeWithIngredients)
        TestCase.assertEquals(defaultRecipe.copy(isSaved = true), result)
    }

    @Test
    fun `recipe to recipe with ingredients test` () {
        val recipeDbMapper = RecipeDbMapper()
        val result = recipeDbMapper.recipeToRecipeWithIngredients(defaultRecipe)
        TestCase.assertEquals(recipeWithIngredients, result)
    }
}