package com.example.recipes.data.database

import androidx.room.*

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM Recipe")
    fun getRecipesWithIngredients(): List<RecipeWithIngredients>

    @Transaction
    @Query("SELECT * FROM Recipe WHERE recipeId = :id")
    fun getRecipeById(id: Int): RecipeWithIngredients?

    @Transaction
    @Query("SELECT * FROM Recipe WHERE title LIKE '%' || :title || '%'")
    fun getRecipeByRequest(title: String): List<RecipeWithIngredients>

    @Insert
    suspend fun insertRecipe(recipeDb: RecipeDb): Long

    @Insert
    suspend fun insertIngredient(ingredientsDb: IngredientsDb): Long

    @Insert
    suspend fun insertRecipeIngredientsCrossRef(recipeIngredientsCrossRef: RecipeIngredientsCrossRef)

    @Delete
    suspend fun deleteRecipe(recipeDb: RecipeDb)

    @Delete
    suspend fun deleteIngredient(ingredientsDb: IngredientsDb)

    @Delete
    suspend fun deleteRecipeIngredientsCrossRef(recipeIngredientsCrossRef: RecipeIngredientsCrossRef)

    @Update
    suspend fun updateRecipe(recipeDb: RecipeDb)

    @Update
    suspend fun updateIngredient(ingredientsDb: IngredientsDb)

    @Transaction
    suspend fun updateRecipeWithIngredients(recipeWithIngredients: RecipeWithIngredients) {
        val oldRecipe = getRecipeById(recipeWithIngredients.recipeDb.recipeId)
        if (oldRecipe != null) {
            recipeWithIngredients.ingredientsDb.forEach {
                if (oldRecipe.ingredientsDb.find { oldIngredient ->
                        oldIngredient.ingredientsId == it.ingredientsId
                    } == null) {
                    val id = insertIngredient(it)
                    insertRecipeIngredientsCrossRef(
                        RecipeIngredientsCrossRef(
                            recipeId = recipeWithIngredients.recipeDb.recipeId.toLong(),
                            ingredientsId = id
                        )
                    )
                } else {
                    updateIngredient(it)
                }
            }
            oldRecipe.ingredientsDb.forEach {
                if (!recipeWithIngredients.ingredientsDb.contains(it)) {
                    deleteIngredient(it)
                }
            }

            updateRecipe(recipeWithIngredients.recipeDb)
        }

    }
}























































































































































