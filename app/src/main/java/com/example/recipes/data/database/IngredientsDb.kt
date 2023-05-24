package com.example.recipes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredients")
data class IngredientsDb (
    @PrimaryKey(autoGenerate = true)
    val ingredientsId: Int,
    val amount: Double,
    val unit: String,
    val name: String
        )