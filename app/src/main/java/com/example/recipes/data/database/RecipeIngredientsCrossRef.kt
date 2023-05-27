package com.example.recipes.data.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "IngredientsToRecipes",
    foreignKeys = [ForeignKey(
        entity = RecipeDb::class,
        parentColumns = arrayOf("recipeId"),
        childColumns = arrayOf("recipeId"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = IngredientsDb::class,
        parentColumns = arrayOf("ingredientsId"),
        childColumns = arrayOf("ingredientsId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.RESTRICT
    )
    ],
    primaryKeys = ["recipeId", "ingredientsId"]
)
data class RecipeIngredientsCrossRef(
    val recipeId: Long,
    val ingredientsId: Long
)