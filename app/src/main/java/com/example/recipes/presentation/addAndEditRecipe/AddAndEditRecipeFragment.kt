package com.example.recipes.presentation.addAndEditRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.R
import com.example.recipes.RecipeApplication
import com.example.recipes.databinding.AddAndEditRecipeFragmentBinding
import com.example.recipes.domain.RecipeDataSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.fakedatasource.FakeDataSource
import com.example.recipes.domain.mappers.RecipeDbMapper
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment.Companion.ADDED_INGREDIENT_REQUEST_KEY
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment.Companion.EDITED_INGREDIENT_REQUEST_KEY
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment.Companion.INGREDIENT_BUNDLE_KEY
import com.example.recipes.presentation.recipe.RecipeArguments
import com.example.recipes.presentation.recipe.RecipeFragment


class AddAndEditRecipeFragment : Fragment() {
    private lateinit var binding: AddAndEditRecipeFragmentBinding
    private val viewModel: AddAndEditRecipeViewModel by viewModels {
        AddAndEditRecipeViewModel.AddAndEditRecipeFactory(
//            Repository(
//                RapidApiSource(retrofitService, RecipeDtoMapper()),
//                RecipeDataSource(
//                    (activity?.application as RecipeApplication).database.recipeDao(),
//                    RecipeDbMapper()))
//                )
            Repository(
                FakeDataSource(), RecipeDataSource(
                    (activity?.application as RecipeApplication).database.recipeDao(),
                    RecipeDbMapper()
                )
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = AddAndEditRecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager.setFragmentResultListener(
            ADDED_INGREDIENT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val ingredient: Ingredient? = bundle.getParcelable(INGREDIENT_BUNDLE_KEY)
            ingredient?.let {
                viewModel.addIngredient(it)
            }
        }
        parentFragmentManager.setFragmentResultListener(
            EDITED_INGREDIENT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val ingredient: Ingredient? = bundle.getParcelable(INGREDIENT_BUNDLE_KEY)
            ingredient?.let {
                viewModel.editIngredient(it)
            }
        }
        val adapter = AddAndEditRecipeAdapter(
            onItemClicked = {
                viewModel.setEditingIngredient(it)
                parentFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, AddAndEditIngredientsFragment.newInstance(it))
                    .commit()
            },
            onItemDeleted = { viewModel.deleteIngredient(it) })


        binding.recipeTitle.addTextChangedListener {
            viewModel.setTitle(it.toString())
        }
        viewModel.title.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.recipeTitle.error = getString(R.string.fill_field)
            }
        }
        binding.readyInTime.addTextChangedListener {
            viewModel.setReadyInMinutes(it.toString())
        }
        viewModel.readyInMinutes.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.readyInTime.error = getString(R.string.fill_field)
            }
        }
        binding.recipeInstructions.addTextChangedListener {
            viewModel.setInstructions(it.toString())
        }
        viewModel.instructions.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.recipeInstructions.error = getString(R.string.fill_field)
            }
        }
        viewModel.extendedIngredients.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.ingredientsListContainer.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.error = getString(R.string.fill_field)
            } else {
                binding.ingredientsListContainer.isVisible = it.value.isNotEmpty()
            }
            adapter.submitList(it.value)
        }

        viewModel.errorOfSave.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(it), Toast.LENGTH_LONG)
                .show()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, AddAndEditIngredientsFragment.newInstance(null))
                .commit()
        }
        binding.saveButton.setOnClickListener {
            viewModel.validateAndSaveRecipe()
        }
        viewModel.id.observe(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, RecipeFragment.newInstance(RecipeArguments(it, false)))
                .commit()
        }
    }

}
