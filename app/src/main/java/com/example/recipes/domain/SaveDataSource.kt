package com.example.recipes.domain

interface SaveDataSource: BaseDataSource {

    suspend fun saveRecipe (id: Int)
}