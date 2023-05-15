package com.example.recipes.domain.fakedatasource

import com.example.recipes.domain.BaseDataSource
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import kotlinx.coroutines.delay

class FakeDataSource : BaseDataSource {
    private val defaultIngredients1 = Ingredient (
        id = 11,
        amount = 0.666667,
        unit = "",
        name = "tomato"
    )
    private val defaultIngredients2 = Ingredient (
        id = 12,
        amount = 3.5,
        unit = "pound",
        name = "beef tenderloin"
    )
    private val defaultIngredients3 = Ingredient (
        id = 13,
        amount = 1.5,
        unit = "teaspoon",
        name = "red pepper"
    )

    private val defaultRecipe = Recipe(
        id = 1,
        title = "Beef",
        image = "https://images.immediate.co.uk/production/volatile/sites/30/2013/05/Puttanesca-fd5810c.jpg?quality=90&webp=true&resize=375,341",
        instructions = "<ol><li>For spice rub: Combine all ingredients in small bowl. Do ahead: Can be made 2 days ahead.</li><li> Store airtight at room temperature. For chimichurri sauce: Combine first 8 ingredients in blender; blend until almost smooth.</li></ol> Add 1/4 of parsley, 1/4 of cilantro, and 1/4 of mint; blend until incorporated. Add remaining herbs in 3 more additions, pureeing until almost smooth after each addition. Do ahead: Can be made 3 hours ahead. Cover; chill. For beef tenderloin: Let beef stand at room temperature 1 hour. Prepare barbecue (high heat). Pat beef dry with paper towels; brush with oil. Sprinkle all over with spice rub, using all of mixture (coating will be thick). Place beef on grill; sear 2 minutes on each side. Reduce heat to medium-high. Grill uncovered until instant-read thermometer inserted into thickest part of beef registers 130F for medium-rare, moving beef to cooler part of grill as needed to prevent burning, and turning occasionally, about 40 minutes. Transfer to platter; cover loosely with foil and let rest 15 minutes. Thinly slice beef crosswise. Serve with chimichurri sauce. *Available at specialty foods stores and from tienda.com.",
        readyInMinutes = 45,
        extendedIngredients = listOf(defaultIngredients1, defaultIngredients2, defaultIngredients3)
    )

    private val defaultRecipe2 = Recipe(
        id = 2,
        title = "Spicy squid ragu with pasta & clams, parmesan baked with tomatoes",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4ehfDVe_Y5YuvJ7oc14SWbndJyWn5Ya49cQ&usqp=CAU",
        instructions = "",
        readyInMinutes = 65,
        extendedIngredients = listOf()
    )
    private val defaultRecipe3 = Recipe(
        id = 3,
        title = "Pasta verde with tomatoes, basil and garlic",
        image = "https://www.allrecipes.com/thmb/mvO1mRRH1zTz1SvbwBCTz78CRJI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/67700_RichPastaforthePoorKitchen_ddmfs_4x3_2284-220302ec8328442096df370dede357d7.jpg",
        instructions = "wioeuwioefuw",
        readyInMinutes = 25,
        extendedIngredients = listOf()
        )
    private val list = listOf(
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

    override suspend fun getRandomRecipe(): List<Recipe> {
        delay(1000)
        return list
    }

    override suspend fun getRecipeById(id: Int): Recipe {
        delay(1000)
        return list.first { it.id == id }
    }

    override suspend fun getRecipeByRequest(title: String): List<Recipe> {
        delay(1000)
        return list.filter { it.title.contains(title, ignoreCase = true) }
    }
}