package com.example.recipes.domain

import com.example.recipes.domain.models.Recipe

class Repository (
    private val baseDataSource: BaseDataSource,
    private val saveDataSource: SaveDataSource) {
    suspend fun getRandomRecipe(): List<Recipe> {
        return baseDataSource.getRandomRecipe()
    }
    suspend fun getRecipeById(id: Int): Recipe {
        val recipe = saveDataSource.getRecipeById(id) ?: baseDataSource.getRecipeById(id)
        return recipe ?: throw IllegalArgumentException()
    }

    suspend fun getRecipeByRequest(title: String): List<Recipe> {
        return baseDataSource.getRecipeByRequest(title)
    }
    suspend fun saveRecipe (recipe: Recipe) {
        return saveDataSource.saveRecipe(recipe)
    }

    suspend fun deleteRecipe (recipe: Recipe) {
        return saveDataSource.deleteRecipe(recipe)
    }
}