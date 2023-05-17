package com.example.recipes

import android.app.Application
import com.example.recipes.data.database.RecipeRoomDatabase

class RecipeApplication : Application() {

    val database: RecipeRoomDatabase by lazy { RecipeRoomDatabase.getDatabase(this) }
}