package com.example.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recipes.databinding.ActivityMainBinding
import com.example.recipes.presentation.addAndEditRecipe.AddAndEditRecipeFragment
import com.example.recipes.presentation.recipeList.RecipeListFragment


enum class Position { RECIPES, SEARCH, ADD, FAVORITES_RECIPES }
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(Position.RECIPES)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_recipes -> changeFragment(Position.RECIPES)
                R.id.action_search -> changeFragment(Position.SEARCH)
                R.id.action_add_recipe -> changeFragment(Position.ADD)
                R.id.action_favorites -> changeFragment(Position.FAVORITES_RECIPES)
            }
            true
        }
    }
    private fun changeFragment(position: Position) {
        val newFragment: Fragment = when (position) {
            Position.RECIPES -> RecipeListFragment.newInstance(false)
            Position.SEARCH -> RecipeListFragment.newInstance(false)
            Position.ADD -> AddAndEditRecipeFragment()
            Position.FAVORITES_RECIPES -> RecipeListFragment.newInstance(true)
        }
       supportFragmentManager.beginTransaction()
                .replace(R.id.container, newFragment).commit()
    }
}