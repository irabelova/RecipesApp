package com.example.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recipes.databinding.ActivityMainBinding
import com.example.recipes.presentation.addAndEditRecipe.AddAndEditRecipeFragment
import com.example.recipes.presentation.recipeList.RecipeListFragment
import com.example.recipes.presentation.video.VideoFragment


enum class Position { RECIPES, VIDEO, ADD, FAVORITES_RECIPES }
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
                R.id.action_video -> changeFragment(Position.VIDEO)
                R.id.action_add_recipe -> changeFragment(Position.ADD)
                R.id.action_favorites -> changeFragment(Position.FAVORITES_RECIPES)
            }
            true
        }
    }
    private fun changeFragment(position: Position) {
        val newFragment: Fragment = when (position) {
            Position.RECIPES -> RecipeListFragment.newInstance(false)
            Position.VIDEO -> VideoFragment()
            Position.ADD -> AddAndEditRecipeFragment()
            Position.FAVORITES_RECIPES -> RecipeListFragment.newInstance(true)
        }
       supportFragmentManager.beginTransaction()
                .replace(R.id.container, newFragment).commit()
    }
}