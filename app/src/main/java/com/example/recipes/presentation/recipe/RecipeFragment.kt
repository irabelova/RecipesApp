package com.example.recipes.presentation.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.recipes.R
import com.example.recipes.RecipeApplication
import com.example.recipes.databinding.RecipeFragmentBinding
import com.example.recipes.domain.RecipeDataSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.fakedatasource.FakeDataSource
import com.example.recipes.domain.mappers.RecipeDbMapper


class RecipeFragment : Fragment() {
    private lateinit var binding: RecipeFragmentBinding
    private var menu: Menu? = null
    private val viewModel: RecipeViewModel by viewModels() {
        RecipeViewModel.RecipeFactory(
//            Repository(
//                RapidApiSource(retrofitService, RecipeDtoMapper()),
//                RecipeDataSource(
//                    (activity?.application as RecipeApplication).database.recipeDao(),
//                    RecipeDbMapper()
//                )
//            ),
            Repository(
                FakeDataSource(), RecipeDataSource(
                    (activity?.application as RecipeApplication).database.recipeDao(),
                    RecipeDbMapper()
                )
            ),
            requireArguments().getParcelable(RECIPE_ARGUMENTS_KEY)!!
        )
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
                            error(R.drawable.ic_broken_image)
                        }
                    }
                    val convertedIngredients =
                        viewModel.convertIngredients(it.recipe.extendedIngredients)
                    binding.extendedIngredients.text = convertedIngredients
                    val convertedInstructions = viewModel.convertInstructions(it.recipe)
                    binding.instructions.text = convertedInstructions
                    binding.recipeTime.text = binding.root.context.getString(
                        R.string.ready_in_minutes,
                        it.recipe.readyInMinutes
                    )
                    showToolbar()
                    binding.toolbar.title = it.recipe.title
                    binding.expandedTitle.text = it.recipe.title
                    binding.fab.setImageResource(
                        if (it.recipe.isSaved) R.drawable.ic_favorite
                        else R.drawable.ic_like
                    )

                }
                RecipeUiModel.Error -> {
                    binding.statusContainer.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }
            }
        }
        viewModel.errorOfSave.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), getString(it), Toast.LENGTH_LONG)
                    .show()
        }
        viewModel.deleteEvent.observe(viewLifecycleOwner) {
            parentFragmentManager.setFragmentResult(DELETE_RECIPE_REQUEST_KEY, bundleOf())
        }
        binding.fab.setOnClickListener {
            viewModel.saveOrDeleteRecipe()
        }
    }

    private fun showToolbar() {
        var isShow = false
        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            if (scrollRange + verticalOffset == 0) {
                isShow = true
                toggleItemVisibility(R.id.like_item, true)
                binding.toolbar.visibility = View.VISIBLE
                binding.expandedTitle.isVisible = false
            } else if (isShow) {
                isShow = false
                toggleItemVisibility(R.id.like_item, false)
                binding.expandedTitle.isVisible = true
                binding.toolbar.visibility = View.INVISIBLE
            }
        }
    }


    private fun toggleItemVisibility(id: Int, value: Boolean) {
        val item = menu?.findItem(id)
        item?.isVisible = value
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
