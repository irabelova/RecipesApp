package com.example.recipes.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe (
        val id: Int,
        val title: String,
        val image: String?,
        val instructions: String,
        val readyInMinutes: Int,
        val extendedIngredients: List<Ingredient>,
        val isSaved: Boolean = false
        ): Parcelable