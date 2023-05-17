package com.example.recipes.data.database

import androidx.room.Entity

@Entity(tableName = "IngredientsToRecipes", primaryKeys = ["recipeId", "ingredientsId"])
data class RecipeIngredientsCrossRef(
    val recipeId: Long,
    val ingredientsId: Long
)