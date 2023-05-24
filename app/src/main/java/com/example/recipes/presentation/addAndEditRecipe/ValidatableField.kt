package com.example.recipes.presentation.addAndEditRecipe

data class ValidatableField<T>(
    val value: T,
    val showError: Boolean = false
)
