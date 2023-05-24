package com.example.recipes.presentation.addAndEditRecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.recipes.databinding.AddAndEditIngredientBinding
import com.example.recipes.domain.models.Ingredient

class AddAndEditRecipeAdapter(
    private val onItemClicked: (Ingredient) -> Unit,
    private val onItemDeleted: (Ingredient) -> Unit
) :
    ListAdapter<Ingredient, AddAndEditRecipeAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            AddAndEditIngredientBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current, onItemDeleted)
    }

    class ItemViewHolder(private var binding: AddAndEditIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient, onItemDeleted: (Ingredient) -> Unit) {
            binding.itemName.text = ingredient.name
            binding.itemAmount.text = ingredient.amount.toString()
            binding.itemUnit.text = ingredient.unit
            binding.deleteButton.setOnClickListener {
                onItemDeleted (ingredient)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                // TODO: Fix compare condition
                return oldItem.name == newItem.name
            }
        }
    }
}