package com.example.recipes.domain.fakedatasource

import com.example.recipes.domain.BaseDataSource
import com.example.recipes.domain.models.Recipe
import kotlinx.coroutines.delay

class FakeDataSource : BaseDataSource {
    private val defaultRecipe = Recipe(
        id = 1,
        title = "Biff ",
        image = "https://images.immediate.co.uk/production/volatile/sites/30/2013/05/Puttanesca-fd5810c.jpg?quality=90&webp=true&resize=375,341",
        instructions = "",
        readyInMinutes = 45,
        extendedIngredients = listOf()
    )
    private val defaultRecipe2 = Recipe(
        id = 2,
        title = "Pasta with mushrooms, broccolli and parmesan baked on pan and this is so tasty that I can imagine what to say",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4ehfDVe_Y5YuvJ7oc14SWbndJyWn5Ya49cQ&usqp=CAU",
        instructions = "",
        readyInMinutes = 65,
        extendedIngredients = listOf()
    )
    private val defaultRecipe3 = Recipe(
        id = 3,
        title = "Pasta verde with tomatoes, basil and garlic, baked under parmesan",
        image = "https://www.allrecipes.com/thmb/mvO1mRRH1zTz1SvbwBCTz78CRJI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/67700_RichPastaforthePoorKitchen_ddmfs_4x3_2284-220302ec8328442096df370dede357d7.jpg",
        instructions = "wioeuwioefuw",
        readyInMinutes = 25,
        extendedIngredients = listOf()
        )

    override suspend fun getRandomRecipe(): List<Recipe> {
        delay(1000)
        return listOf(
            defaultRecipe,
            defaultRecipe2,
            defaultRecipe3,
            defaultRecipe,
            defaultRecipe2,
            defaultRecipe3,
            defaultRecipe,
            defaultRecipe2,
            defaultRecipe3
        )
    }

    override suspend fun getRecipeById(id: Int): Recipe {
        delay(1000)
        return defaultRecipe
    }
}