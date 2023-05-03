package com.example.recipes.domain

import com.example.recipes.domain.models.Recipe

interface BaseDataSource {

    suspend fun getRandomRecipe(): List<Recipe>

    suspend fun getRecipeById(id: Int): Recipe
}