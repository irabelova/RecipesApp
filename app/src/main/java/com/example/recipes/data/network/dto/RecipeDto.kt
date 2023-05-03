package com.example.recipes.data.network.dto

data class RecipeDto(
        val id: Int,
        val title: String,
        val image: String?,
        val instructions: String,
        val readyInMinutes: Int,
        val extendedIngredients: List<IngredientDto>
    )
