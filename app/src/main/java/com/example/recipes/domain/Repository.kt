package com.example.recipes.domain

import com.example.recipes.domain.models.Recipe

class Repository (private val baseDataSource: BaseDataSource) {
    suspend fun getRandomRecipe(): List<Recipe> {
        return baseDataSource.getRandomRecipe()
    }
    suspend fun getRecipeById(id: Int): Recipe {
        return baseDataSource.getRecipeById(id)
    }

    suspend fun getRecipeByRequest(title: String): List<Recipe> {
        return baseDataSource.getRecipeByRequest(title)
    }
}