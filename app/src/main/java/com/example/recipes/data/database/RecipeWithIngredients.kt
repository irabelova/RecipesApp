package com.example.recipes.data.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RecipeWithIngredients (
    @Embedded val recipeDb: RecipeDb,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "ingredientsId",
        associateBy = Junction(RecipeIngredientsCrossRef::class)
    )
    val ingredientsDb: List<IngredientsDb>
)