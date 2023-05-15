package com.example.recipes.domain

import com.example.recipes.data.network.RecipeApiService
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.domain.models.Recipe

class RapidApiSource (
    private val retrofitService: RecipeApiService,
    private val recipeDtoMapper: RecipeDtoMapper
    ): BaseDataSource {

    override suspend fun getRandomRecipe(): List<Recipe> {
        val randomRecipeDto =  retrofitService.getRandomRecipe()
        return recipeDtoMapper.randomRecipeDtoToRecipes(randomRecipeDto)
    }

    override suspend fun getRecipeById(id: Int): Recipe {
        val recipe = retrofitService.getRecipe(id)
        return recipeDtoMapper.recipeDtoToRecipe(recipe)
    }

    override suspend fun getRecipeByRequest(title: String): List<Recipe> {
        val recipeListDto = retrofitService.getRecipeByRequest(title = title)
        val recipeByRequestList = recipeDtoMapper.recipeListDtoToRecipesByRequest(recipeListDto)
        return recipeByRequestList.map {
            getRecipeById(it.id)
        }
    }
}