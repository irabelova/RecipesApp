package com.example.recipes.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient (
    val id: Int,
    val amount: Double,
    val unit: String,
    val name: String
): Parcelable
