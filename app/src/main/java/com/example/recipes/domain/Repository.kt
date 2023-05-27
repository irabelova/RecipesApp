package com.example.recipes.domain

import com.example.recipes.domain.models.Recipe

class Repository (
    private val baseDataSource: BaseDataSource,
    private val saveDataSource: SaveDataSource) {
    suspend fun getRandomRecipe(showOnlySaved: Boolean): List<Recipe> {
        return if (showOnlySaved) {
            saveDataSource.getRandomRecipe()
        } else {
            baseDataSource.getRandomRecipe()
        }
    }
    suspend fun getRecipeById(id: Int, isFromApiSource: Boolean): Recipe {
        return if (isFromApiSource) {
            val recipe = saveDataSource.getRecipeById(id) ?: baseDataSource.getRecipeById(id)
            recipe ?: throw IllegalArgumentException()
        } else {
            val recipe = saveDataSource.getRecipeById(id)
            recipe ?: throw IllegalArgumentException()
        }
    }

    suspend fun getRecipeByRequest(title: String, showOnlySaved: Boolean): List<Recipe> {
        return if (showOnlySaved) {
            saveDataSource.getRecipeByRequest(title)
        } else {
            baseDataSource.getRecipeByRequest(title)
        }
    }
    suspend fun saveRecipe (recipe: Recipe): Long {
        return saveDataSource.saveRecipe(recipe)
    }

    suspend fun deleteRecipe (recipe: Recipe) {
         saveDataSource.deleteRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe) {
        saveDataSource.updateRecipe(recipe)
    }
}