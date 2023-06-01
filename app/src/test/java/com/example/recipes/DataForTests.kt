package com.example.recipes

import com.example.recipes.data.database.IngredientsDb
import com.example.recipes.data.database.RecipeDb
import com.example.recipes.data.database.RecipeWithIngredients
import com.example.recipes.data.network.dto.*
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import com.example.recipes.domain.models.RecipeByRequest
import com.example.recipes.domain.models.Video

internal val video = Video(
    youTubeId = "OMcDk2_4LSk",
    title = "Android Jetpack: LiveData",
    thumbnail = "https://i.ytimg.com/vi/OMcDk2_4LSk/mqdefault.jpg"
)

internal val videoDto = VideoDto(
    youTubeId = "OMcDk2_4LSk",
    title = "Android Jetpack: LiveData",
    thumbnail = "https://i.ytimg.com/vi/OMcDk2_4LSk/mqdefault.jpg"
)

internal val defaultIngredient = Ingredient(
    id = 11, amount = 200.0, unit = "g", name = "pasta"
)


internal val defaultRecipe = Recipe(
    id = 0,
    title = "Pasta",
    image = null,
    instructions = "Cook the pasta, adding the broccoli for the final 4-5 minutes and cooking until tender. Drain well, then heat the grill",
    readyInMinutes = 45,
    extendedIngredients = listOf(defaultIngredient)
)

internal val ingredientDto = IngredientDto(
    id = 11,
    amount = 200.0,
    unit = "g",
    name = "pasta"
)


internal val recipeDto = RecipeDto(
    id = 0,
    title = "Pasta",
    image = null,
    instructions = "Cook the pasta, adding the broccoli for the final 4-5 minutes and cooking until tender. Drain well, then heat the grill",
    readyInMinutes = 45,
    extendedIngredients = listOf(ingredientDto)
)

internal val recipeByRequestDto = RecipeByRequestDto(
    id = 0,
    title = "Pasta",
    image = null,
)

internal val recipeByRequest = RecipeByRequest(
    id = 0,
    title = "Pasta",
    image = null,
)
internal val ingredientDb = IngredientsDb(
    ingredientsId = 11,
    amount = 200.0,
    unit = "g",
    name = "pasta"
)


internal val recipeDb = RecipeDb(
    recipeId = 0,
    title = "Pasta",
    image = null,
    instructions = "Cook the pasta, adding the broccoli for the final 4-5 minutes and cooking until tender. Drain well, then heat the grill",
    readyInMinutes = 45,
)

internal val recipeWithIngredients = RecipeWithIngredients(
    recipeDb = recipeDb,
    ingredientsDb = listOf(ingredientDb)
)
