package com.example.recipes

import com.example.recipes.data.network.dto.RandomRecipeDto
import com.example.recipes.data.network.dto.RecipeListDto
import com.example.recipes.domain.mappers.RecipeDtoMapper
import junit.framework.TestCase
import org.junit.Test

internal class RecipeDtoMapperUnitTests {

    private val recipeList = listOf(defaultRecipe)
    private val recipeDtoList = listOf(recipeDto)
    private val randomRecipeDto = RandomRecipeDto(recipeDtoList)
    private val recipeByRequestListDto = listOf(recipeByRequestDto)
    private val recipeListDto = RecipeListDto(recipeByRequestListDto)
    private val recipeByRequestList = listOf(recipeByRequest)


    @Test
    fun `random recipe dto to recipes test`() {
        val recipeDtoMapper = RecipeDtoMapper()
        val result = recipeDtoMapper.randomRecipeDtoToRecipes(randomRecipeDto)
        TestCase.assertEquals(recipeList, result)
    }

    @Test
    fun `recipe list dto to recipes by request test`() {
        val recipeDtoMapper = RecipeDtoMapper()
        val result = recipeDtoMapper.recipeListDtoToRecipesByRequest(recipeListDto)
        TestCase.assertEquals(recipeByRequestList, result)
    }

    @Test
    fun `recipe dto to recipe test`() {
        val recipeDtoMapper = RecipeDtoMapper()
        val result = recipeDtoMapper.recipeDtoToRecipe(recipeDto)
        TestCase.assertEquals(defaultRecipe, result)
    }
}