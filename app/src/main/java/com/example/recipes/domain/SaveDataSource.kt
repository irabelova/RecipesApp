package com.example.recipes.domain

import com.example.recipes.domain.models.Recipe

interface SaveDataSource: BaseDataSource {

    suspend fun saveRecipe (recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)
}