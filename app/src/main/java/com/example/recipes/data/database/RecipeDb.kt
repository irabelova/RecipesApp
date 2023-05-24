package com.example.recipes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
data class RecipeDb (
    @PrimaryKey(autoGenerate = true)
    val recipeId: Int,
    val title: String,
    val image: String?,
    val instructions: String,
    val readyInMinutes: Int,
        )