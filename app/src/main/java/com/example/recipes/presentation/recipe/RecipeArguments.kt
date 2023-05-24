package com.example.recipes.presentation.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeArguments(
    val id: Int,
    val isFromApiSource: Boolean
) : Parcelable
