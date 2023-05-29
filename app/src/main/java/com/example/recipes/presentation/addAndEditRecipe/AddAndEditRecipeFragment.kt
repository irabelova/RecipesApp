package com.example.recipes.presentation.addAndEditRecipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.recipes.R
import com.example.recipes.RecipeApplication
import com.example.recipes.data.network.RecipeApi.retrofitService
import com.example.recipes.databinding.AddAndEditRecipeFragmentBinding
import com.example.recipes.domain.RapidApiSource
import com.example.recipes.domain.RecipeDataSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.mappers.RecipeDbMapper
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.domain.mappers.VideoDtoMapper
import com.example.recipes.domain.models.Ingredient
import com.example.recipes.domain.models.Recipe
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment.Companion.ADDED_INGREDIENT_REQUEST_KEY
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment.Companion.EDITED_INGREDIENT_REQUEST_KEY
import com.example.recipes.presentation.addAndEditIngredients.AddAndEditIngredientsFragment.Companion.INGREDIENT_BUNDLE_KEY
import com.example.recipes.presentation.recipe.RecipeArguments
import com.example.recipes.presentation.recipe.RecipeFragment
import com.example.recipes.utils.convertInstructions


class AddAndEditRecipeFragment : Fragment() {
    private lateinit var binding: AddAndEditRecipeFragmentBinding
    private val viewModel: AddAndEditRecipeViewModel by viewModels {
        AddAndEditRecipeViewModel.AddAndEditRecipeFactory(
            Repository(
                RapidApiSource(retrofitService, RecipeDtoMapper(), VideoDtoMapper()),
                RecipeDataSource(
                    (activity?.application as RecipeApplication).database.recipeDao(),
                    RecipeDbMapper()),
                RapidApiSource(retrofitService, RecipeDtoMapper(), VideoDtoMapper())
                ), arguments?.getParcelable(EDIT_RECIPE_KEY)
//            Repository(
//                FakeDataSource(), RecipeDataSource(
//                    (activity?.application as RecipeApplication).database.recipeDao(),
//                    RecipeDbMapper()
//                ), FakeDataSource()
//            ), arguments?.getParcelable(EDIT_RECIPE_KEY)
        )
    }
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setUri(uri)
            }
        }

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
            if(it.value != binding.recipeTitle.text.toString()) {
                binding.recipeTitle.setText(it.value)
            }
        }
        binding.readyInTime.addTextChangedListener {
            viewModel.setReadyInMinutes(it.toString())
        }
        viewModel.readyInMinutes.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.readyInTime.error = getString(R.string.fill_field)
            }
            if(it.value != binding.readyInTime.text.toString()) {
                binding.readyInTime.setText(it.value)
            }
        }
        binding.recipeInstructions.addTextChangedListener {
            viewModel.setInstructions(it.toString())
        }
        viewModel.instructions.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.recipeInstructions.error = getString(R.string.fill_field)
            }
            if(it.value != binding.recipeInstructions.text.toString()) {
                binding.recipeInstructions.setText(it.value.convertInstructions())
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
        binding.addImage.setOnClickListener {
            addImage()
        }

        viewModel.uriString.observe(viewLifecycleOwner) {
            it.let {
                binding.addImage.load(it) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_add_recipe)
                }
                if (it != null) {
                    binding.addImageText.visibility = View.GONE
                    binding.deleteImage.visibility = View.VISIBLE
                } else {
                    binding.addImageText.visibility = View.VISIBLE
                    binding.deleteImage.visibility = View.GONE
                }
            }
        }
        binding.deleteImage.setOnClickListener {
            viewModel.setUri(null)
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

    private fun addImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    companion object {
        private const val EDIT_RECIPE_KEY = "EDIT_RECIPE_KEY"
        fun newInstance(recipe: Recipe?): AddAndEditRecipeFragment {
            val addAndEditRecipeFragment = AddAndEditRecipeFragment()
            addAndEditRecipeFragment.arguments = bundleOf(
                EDIT_RECIPE_KEY to recipe
            )
            return addAndEditRecipeFragment
        }
    }

}
