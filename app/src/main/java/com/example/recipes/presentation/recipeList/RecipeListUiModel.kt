package com.example.recipes.presentation.recipeList

import com.example.recipes.domain.models.Recipe

sealed interface RecipeListUiModel {
    object Loading: RecipeListUiModel
    data class Data(val recipes: List<Recipe>): RecipeListUiModel
    object Error: RecipeListUiModel
}