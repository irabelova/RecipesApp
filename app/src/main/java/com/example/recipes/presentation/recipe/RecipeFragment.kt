package com.example.recipes.presentation.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.recipes.R
import com.example.recipes.RecipeApplication
import com.example.recipes.data.network.RecipeApi.retrofitService
import com.example.recipes.databinding.RecipeFragmentBinding
import com.example.recipes.domain.RapidApiSource
import com.example.recipes.domain.RecipeDataSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.mappers.RecipeDbMapper
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.domain.mappers.VideoDtoMapper
import com.example.recipes.presentation.addAndEditRecipe.AddAndEditRecipeFragment
import com.example.recipes.utils.convertIngredients
import com.example.recipes.utils.convertInstructions


class RecipeFragment : Fragment() {
    private lateinit var binding: RecipeFragmentBinding
    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModel.RecipeFactory(
            Repository(
                RapidApiSource(retrofitService, RecipeDtoMapper(), VideoDtoMapper()),
                RecipeDataSource(
                    (activity?.application as RecipeApplication).database.recipeDao(),
                    RecipeDbMapper()
                ),
                RapidApiSource(retrofitService, RecipeDtoMapper(), VideoDtoMapper())
            ), requireArguments().getParcelable(RECIPE_ARGUMENTS_KEY)!!
        )
//            Repository(
//                FakeDataSource(), RecipeDataSource(
//                    (activity?.application as RecipeApplication).database.recipeDao(),
//                    RecipeDbMapper()
//                ), FakeDataSource()
//            ), requireArguments().getParcelable(RECIPE_ARGUMENTS_KEY)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = RecipeFragmentBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                RecipeUiModel.Loading -> {
                    binding.statusContainer.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                }
                is RecipeUiModel.Data -> {
                    binding.statusContainer.visibility = View.GONE
                    it.recipe.image.let { image ->
                        binding.expandedImage.load(image) {
                            placeholder(R.drawable.loading_animation)
                        }
                    }
                    val convertedIngredients = it.recipe.extendedIngredients.convertIngredients()
                    binding.extendedIngredients.text = convertedIngredients
                    val convertedInstructions = it.recipe.instructions.convertInstructions()
                    binding.instructions.text = convertedInstructions
                    binding.recipeTime.text = binding.root.context.getString(
                        R.string.ready_in_minutes, it.recipe.readyInMinutes
                    )
                    showToolbar()
                    binding.toolbar.title = it.recipe.title
                    binding.expandedTitle.text = it.recipe.title
                    if (it.recipe.isSaved) {
                        binding.fab.setImageResource(R.drawable.ic_favorite)
                        binding.toolbar.menu.findItem(R.id.like_item)?.setIcon(
                            R.drawable.ic_favorite
                        )
                        binding.editFab.isVisible = true
                        binding.toolbar.menu.findItem(R.id.edit_item)?.isVisible = true
                    } else {
                        binding.fab.setImageResource(R.drawable.ic_like)
                        binding.toolbar.menu.findItem(R.id.like_item)?.setIcon(
                            R.drawable.ic_like
                        )
                        binding.editFab.isVisible = false
                        binding.toolbar.menu.findItem(R.id.edit_item)?.isVisible = false
                    }
                }
                RecipeUiModel.Error -> {
                    binding.statusContainer.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }
            }
        }
        viewModel.errorOfSave.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(it), Toast.LENGTH_LONG).show()
        }
        viewModel.deleteEvent.observe(viewLifecycleOwner) {
            parentFragmentManager.setFragmentResult(DELETE_RECIPE_REQUEST_KEY, bundleOf())
        }

        binding.fab.setOnClickListener {
            viewModel.saveOrDeleteRecipe()
        }
        viewModel.showAlertDialog.observe(viewLifecycleOwner) {
            showRecipeDialog(it)
        }
        binding.editFab.setOnClickListener {
            editAction()
        }

        binding.toolbar.menu.findItem(R.id.like_item).setOnMenuItemClickListener {
            viewModel.saveOrDeleteRecipe()
            true
        }
        binding.toolbar.menu.findItem(R.id.edit_item).setOnMenuItemClickListener {
            editAction()
            true
        }
    }

    private fun showRecipeDialog(data: RecipeUiModel.Data) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.alert_dialog_message)
        builder.setIcon(R.drawable.ic_delete)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            viewModel.deleteRecipe(data)
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    private fun editAction() {
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container,
                AddAndEditRecipeFragment.newInstance((viewModel.state.value as? RecipeUiModel.Data)?.recipe)
            )
            .commit()
    }

    private fun showToolbar() {
        var isShow = false
        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            if (scrollRange + verticalOffset == 0) {
                isShow = true
                binding.toolbar.visibility = View.VISIBLE
                binding.expandedTitle.isVisible = false
            } else if (isShow) {
                isShow = false
                binding.expandedTitle.isVisible = true
                binding.toolbar.visibility = View.INVISIBLE
            }
        }
    }


    companion object {
        private const val RECIPE_ARGUMENTS_KEY = "RECIPE_ARGUMENTS_KEY"
        const val DELETE_RECIPE_REQUEST_KEY = "DELETE_RECIPE_REQUEST_KEY"
        fun newInstance(recipeArguments: RecipeArguments): RecipeFragment {
            val recipeFragment = RecipeFragment()
            recipeFragment.arguments = bundleOf(
                RECIPE_ARGUMENTS_KEY to recipeArguments
            )
            return recipeFragment
        }
    }
}
