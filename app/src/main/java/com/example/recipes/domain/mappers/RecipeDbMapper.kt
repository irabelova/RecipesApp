package com.example.recipes.domain.mappers

import com.example.recipes.data.database.IngredientsDb
import com.example.recipes.data.database.RecipeDb
import com.example.recipes.data.database.RecipeWithIngredients
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe

class RecipeDbMapper {

    fun recipeWithIngredientsToRecipe (recipeWithIngredients: RecipeWithIngredients): Recipe {
        return recipeDbToRecipe(recipeWithIngredients.recipeDb, recipeWithIngredients.ingredientsDb)
    }

    fun recipeToRecipeWithIngredients (recipe: Recipe): RecipeWithIngredients {
        return RecipeWithIngredients(
            recipeToRecipeDb(recipe),
            recipe.extendedIngredients.map {
                ingredientsToIngredientsDb(it) }
        )
    }

    private fun recipeDbToRecipe(recipeDb: RecipeDb, ingredientsDb: List<IngredientsDb>): Recipe {
        return Recipe(
            id = recipeDb.recipeId,
            title = recipeDb.title,
            image = recipeDb.image,
            instructions = recipeDb.instructions,
            readyInMinutes = recipeDb.readyInMinutes,
            extendedIngredients = ingredientsDb.map {
                ingredientsDbToIngredients(it) },
            isSaved = true
        )
    }
    private fun ingredientsDbToIngredients (ingredientsDb: IngredientsDb): Ingredient {
        return Ingredient (
            id = ingredientsDb.ingredientsId,
            amount = ingredientsDb.amount,
            unit = ingredientsDb.unit,
            name = ingredientsDb.name
            )
    }

    private fun recipeToRecipeDb(recipe: Recipe): RecipeDb {
        return RecipeDb(
            recipeId = recipe.id,
            title = recipe.title,
            image = recipe.image,
            instructions = recipe.instructions,
            readyInMinutes = recipe.readyInMinutes,
        )
    }

    private fun ingredientsToIngredientsDb (ingredients: Ingredient): IngredientsDb {
        return IngredientsDb (
            ingredientsId = ingredients.id,
            amount = ingredients.amount,
            unit = ingredients.unit,
            name = ingredients.name
        )
    }
}