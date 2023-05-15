package com.example.recipes.domain.mappers

import com.example.recipes.data.network.dto.*
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import com.example.recipes.domain.models.RecipeByRequest

class RecipeDtoMapper {

    fun randomRecipeDtoToRecipes(randomRecipeDto: RandomRecipeDto): List<Recipe> {
        return randomRecipeDto.recipes.map {
            recipeDtoToRecipe(it)
        }
    }

    fun recipeListDtoToRecipesByRequest(recipeListDto: RecipeListDto): List<RecipeByRequest> {
        return recipeListDto.results.map {
            recipeByRequestDtoToRecipeByRequest(it)
        }
    }

    fun recipeDtoToRecipe(recipeDto: RecipeDto): Recipe {
        return Recipe(
            id = recipeDto.id,
            title = recipeDto.title,
            image = recipeDto.image,
            instructions = recipeDto.instructions,
            readyInMinutes = recipeDto.readyInMinutes,
            extendedIngredients = recipeDto.extendedIngredients.map { ingredientDtoToIngredient(it) }

        )
    }

    private fun ingredientDtoToIngredient(ingredient: IngredientDto): Ingredient {
        return Ingredient(
            id = ingredient.id,
            amount = ingredient.amount,
            unit = ingredient.unit,
            name = ingredient.name
        )
    }
    private fun recipeByRequestDtoToRecipeByRequest(recipeByRequestDto: RecipeByRequestDto): RecipeByRequest {
        return RecipeByRequest(
            id = recipeByRequestDto.id,
            title = recipeByRequestDto.title,
            image = recipeByRequestDto.image
        )
    }
}