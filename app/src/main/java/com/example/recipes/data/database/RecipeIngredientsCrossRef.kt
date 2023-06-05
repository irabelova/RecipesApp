package com.example.recipes.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "IngredientsToRecipes",
    foreignKeys = [ForeignKey(
        entity = RecipeDb::class,
        parentColumns = arrayOf("recipeId"),
        childColumns = arrayOf("recipeId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = IngredientsDb::class,
        parentColumns = arrayOf("ingredientsId"),
        childColumns = arrayOf("ingredientsId"),
        onDelete = ForeignKey.RESTRICT,
    )
    ],

)
data class RecipeIngredientsCrossRef(
    @PrimaryKey(autoGenerate = true)
    val crossId: Int,
    val recipeId: Long,
    val ingredientsId: Long
)