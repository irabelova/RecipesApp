package com.example.recipes.presentation.recipe

import com.example.recipes.domain.models.Recipe

sealed interface RecipeUiModel {
    object Loading : RecipeUiModel
    data class Data(val recipe: Recipe) : RecipeUiModel
    object Error : RecipeUiModel
}