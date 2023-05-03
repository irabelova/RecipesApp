package com.example.recipes.domain.models

data class Recipe (
        val id: Int,
        val title: String,
        val image: String?,
        val instructions: String,
        val readyInMinutes: Int,
        val extendedIngredients: List<Ingredient>
        )