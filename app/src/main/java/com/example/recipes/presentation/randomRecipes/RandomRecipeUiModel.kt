package com.example.recipes.presentation.randomRecipes

import com.example.recipes.domain.models.Recipe

sealed interface RandomRecipeUiModel {
    object Loading: RandomRecipeUiModel
    data class Data(val recipes: List<Recipe>): RandomRecipeUiModel
    object Error: RandomRecipeUiModel
}