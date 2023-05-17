package com.example.recipes.data.database

import androidx.room.*

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM Recipe")
    fun getRecipesWithIngredients(): List<RecipeWithIngredients>

    @Transaction
    @Query ("SELECT * FROM Recipe WHERE recipeId = :id")
    fun getRecipeById(id: Int): RecipeWithIngredients?

    @Insert
    suspend fun insertRecipe (recipeDb: RecipeDb): Long

    @Insert
    suspend fun insertIngredient (ingredientsDb: IngredientsDb): Long

    @Insert
    suspend fun insertRecipeIngredientsCrossRef(recipeIngredientsCrossRef: RecipeIngredientsCrossRef)

    @Delete
    suspend fun deleteRecipe (recipeDb: RecipeDb)

    @Delete
    suspend fun deleteIngredient (ingredientsDb: IngredientsDb)

    @Delete
    suspend fun deleteRecipeIngredientsCrossRef(recipeIngredientsCrossRef: RecipeIngredientsCrossRef)
}























































































































































