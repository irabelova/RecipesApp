package com.example.recipes.domain

import com.example.recipes.data.database.RecipeDao
import com.example.recipes.data.database.RecipeIngredientsCrossRef
import com.example.recipes.domain.mappers.RecipeDbMapper
import com.example.recipes.domain.models.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeDataSource(
    private val recipeDao: RecipeDao,
    private val recipeDbMapper: RecipeDbMapper
) : SaveDataSource {

    override suspend fun saveRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        val recipeWithIngredients = recipeDbMapper.recipeToRecipeWithIngredients(recipe)
        val ingredientsIds = recipeWithIngredients.ingredientsDb.map {
            recipeDao.insertIngredient(it)
        }
        val recipeId = recipeDao.insertRecipe(recipeWithIngredients.recipeDb)
        ingredientsIds.forEach {
            recipeDao.insertRecipeIngredientsCrossRef(
                RecipeIngredientsCrossRef(
                    recipeId = recipeId,
                    ingredientsId = it
                )
            )
        }
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        val recipeWithIngredients = recipeDbMapper.recipeToRecipeWithIngredients(recipe)
        val ingredientsIds = recipeWithIngredients.ingredientsDb.map{
            it.ingredientsId.toLong()}
        ingredientsIds.forEach {
            recipeDao.deleteRecipeIngredientsCrossRef(
                RecipeIngredientsCrossRef(
                    recipeId = recipeWithIngredients.recipeDb.recipeId.toLong(),
                    ingredientsId = it
                )
            )
        }
        recipeDao.deleteRecipe(recipeWithIngredients.recipeDb)
        recipeWithIngredients.ingredientsDb.forEach {
            recipeDao.deleteIngredient(it)
        }
    }

    override suspend fun getRandomRecipe(): List<Recipe> {
        val recipeWithIngredients = recipeDao.getRecipesWithIngredients()
        return recipeWithIngredients.map {
            recipeDbMapper.recipeWithIngredientsToRecipe(it)
        }
    }

    override suspend fun getRecipeById(id: Int): Recipe? = withContext(Dispatchers.IO) {
        val recipe = recipeDao.getRecipeById(id)
        recipe?.let {
            recipeDbMapper.recipeWithIngredientsToRecipe(it)
        }

    }

    override suspend fun getRecipeByRequest(title: String): List<Recipe> {
        TODO("Not yet implemented")
    }
}