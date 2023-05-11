package com.example.recipes.presentation.randomRecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipes.R
import com.example.recipes.data.network.RecipeApi.retrofitService
import com.example.recipes.databinding.RandomRecipeFragmentBinding
import com.example.recipes.domain.RapidApiSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.fakedatasource.FakeDataSource
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.presentation.recipe.RecipeFragment

class RandomRecipeFragment: Fragment() {
    private lateinit var binding: RandomRecipeFragmentBinding
    private val viewModel: RandomRecipeViewModel by viewModels {
        RandomRecipeViewModel.RandomRecipeFactory(
//            Repository(RapidApiSource(retrofitService, RecipeDtoMapper()))
            Repository(FakeDataSource())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = RandomRecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = RandomRecipeAdapter {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, RecipeFragment.newInstance(it.id))
                .commit()
        }



        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                RandomRecipeUiModel.Loading -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                }
                is RandomRecipeUiModel.Data -> {
                    adapter.submitList(it.recipes)
                    binding.statusImage.visibility = View.GONE
                    binding.recipesRecycleView.adapter = adapter
                }
                RandomRecipeUiModel.Error -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }
            }
        }
    }
}