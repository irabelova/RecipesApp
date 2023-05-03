package com.example.recipes.domain.mappers

import com.example.recipes.data.network.dto.IngredientDto
import com.example.recipes.data.network.dto.RandomRecipeDto
import com.example.recipes.data.network.dto.RecipeDto
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe

class RecipeDtoMapper {

    fun randomRecipeDtoToRecipes (randomRecipeDto: RandomRecipeDto): List<Recipe> {
        return randomRecipeDto.recipes.map {
            recipeDtoToRecipe(it)
        }
    }

    fun recipeDtoToRecipe (recipeDto: RecipeDto): Recipe {
        return Recipe(
            id = recipeDto.id,
            title = recipeDto.title,
            image = recipeDto.image,
            instructions = recipeDto.instructions,
            readyInMinutes = recipeDto.readyInMinutes,
            extendedIngredients = recipeDto.extendedIngredients.map { ingredientDtoToIngredient(it) }

        )
    }
    private fun ingredientDtoToIngredient (ingredient: IngredientDto): Ingredient {
        return Ingredient(
        id = ingredient.id,
        amount = ingredient.amount,
        unit = ingredient.unit,
        name = ingredient.name
        )
    }
}