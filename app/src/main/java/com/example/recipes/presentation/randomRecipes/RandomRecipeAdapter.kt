package com.example.recipes.presentation.randomRecipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.recipes.R
import com.example.recipes.databinding.ViewItemBinding
import com.example.recipes.domain.models.Recipe

class RandomRecipeAdapter (private val clickListener: (Recipe) -> Unit) :
    ListAdapter<Recipe, RandomRecipeAdapter.RandomRecipeViewHolder>(DiffCallback) {

    class RandomRecipeViewHolder(
        private val binding: ViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(recipe: Recipe, clickListener: (Recipe) -> Unit) {
            binding.recipeTitle.text = recipe.title
            binding.recipeTime.text = binding.root.context.getString(R.string.ready_in_minutes, recipe.readyInMinutes)
            binding.card.setOnClickListener { clickListener(recipe) }
            recipe.image.let {
                binding.recipeImage.load(recipe.image) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.title == newItem.title && oldItem.extendedIngredients == newItem.extendedIngredients &&
                    oldItem.instructions == newItem.instructions
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomRecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RandomRecipeViewHolder(
            ViewItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RandomRecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe, clickListener)
    }
}