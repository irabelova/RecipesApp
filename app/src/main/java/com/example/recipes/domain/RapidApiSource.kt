package com.example.recipes.domain

import com.example.recipes.data.network.RecipeApiService
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.domain.mappers.VideoDtoMapper
import com.example.recipes.domain.models.Recipe
import com.example.recipes.domain.models.Video

class RapidApiSource (
    private val retrofitService: RecipeApiService,
    private val recipeDtoMapper: RecipeDtoMapper,
    private val videoDtoMapper: VideoDtoMapper
    ): BaseDataSource, VideoDataSource {

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
        return recipeListDto.results.map {
            getRecipeById(it.id)
        }
    }

    override suspend fun getVideoList(title: String): List<Video> {
        val videoListDto = retrofitService.getVideoList(title = title)
        return videoDtoMapper.videoListDtoToVideoList(videoListDto)
    }
}