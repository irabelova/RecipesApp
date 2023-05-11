package com.example.recipes.presentation.recipe

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.core.view.isVisible
import coil.load
import com.example.recipes.R
import com.example.recipes.data.network.RecipeApi.retrofitService
import com.example.recipes.databinding.RecipeFragmentBinding
import com.example.recipes.domain.RapidApiSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.fakedatasource.FakeDataSource
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.google.android.material.appbar.AppBarLayout


class RecipeFragment (): Fragment() {
    private lateinit var binding: RecipeFragmentBinding
    private var menu: Menu? = null
    private val viewModel: RecipeViewModel by viewModels() {
        RecipeViewModel.RecipeFactory(
//            Repository(RapidApiSource(retrofitService, RecipeDtoMapper())),
            Repository(FakeDataSource()),
            requireArguments().getInt(ID_KEY)
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
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                }
                is RecipeUiModel.Data -> {
                    binding.statusImage.visibility = View.GONE
                    it.recipe.image.let { image ->
                        binding.expandedImage.load(image) {
                            placeholder(R.drawable.loading_animation)
                            error(R.drawable.ic_broken_image)
                        }
                    }
                    val convertedIngredients = viewModel.convertIngredients(it.recipe.extendedIngredients)
                    binding.extendedIngredients.text = convertedIngredients
                    val convertedInstructions = viewModel.convertInstructions(it.recipe)
                    binding.instructions.text = convertedInstructions
                    binding.recipeTime.text = binding.root.context.getString(R.string.ready_in_minutes, it.recipe.readyInMinutes)
                    showToolbar()
                    binding.toolbar.title = it.recipe.title
                    binding.expandedTitle.text = it.recipe.title

                }
                RecipeUiModel.Error -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }
            }
        }
    }

    private fun showToolbar() {
        var isShow = false
        binding.appbar.addOnOffsetChangedListener {appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            if (scrollRange + verticalOffset == 0){
                isShow = true
                toggleItemVisibility (R.id.like_item, true)
                binding.toolbar.visibility = View.VISIBLE
                binding.expandedTitle.isVisible = false
            }
            else if (isShow){
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
        private const val ID_KEY = "ID_KEY"
        fun newInstance(id: Int): RecipeFragment {
            val recipeFragment = RecipeFragment()
            recipeFragment.arguments = bundleOf(
                ID_KEY to id
            )
            return recipeFragment
        }
    }
}
