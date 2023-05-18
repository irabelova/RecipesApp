package com.example.recipes.presentation.recipeList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipes.R
import com.example.recipes.RecipeApplication
import com.example.recipes.data.network.RecipeApi.retrofitService
import com.example.recipes.databinding.RandomRecipeFragmentBinding
import com.example.recipes.domain.RapidApiSource
import com.example.recipes.domain.RecipeDataSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.fakedatasource.FakeDataSource
import com.example.recipes.domain.mappers.RecipeDbMapper
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.presentation.recipe.RecipeFragment


class RecipeListFragment : Fragment() {
    private lateinit var binding: RandomRecipeFragmentBinding
    private val viewModel: RecipeListViewModel by viewModels {
        RecipeListViewModel.RandomRecipeFactory(
//            Repository(
//                RapidApiSource(retrofitService, RecipeDtoMapper()),
//                RecipeDataSource(
//                    (activity?.application as RecipeApplication).database.recipeDao(),
//                    RecipeDbMapper())), requireArguments().getBoolean(SHOW_ONLY_SAVED_FLAG)
//                )
            Repository(
                FakeDataSource(), RecipeDataSource(
                    (activity?.application as RecipeApplication).database.recipeDao(),
                    RecipeDbMapper()
                )
            ), requireArguments().getBoolean(SHOW_ONLY_SAVED_FLAG)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = RandomRecipeFragmentBinding.inflate(inflater, container, false)
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setTitle(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = RecipeListAdapter {
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, RecipeFragment.newInstance(it.id))
                .commit()
        }

        parentFragmentManager.setFragmentResultListener(
            RecipeFragment.DELETE_RECIPE_REQUEST_KEY,
            viewLifecycleOwner) {_, _ ->
            viewModel.getRecipe()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                RecipeListUiModel.Loading -> {
                    binding.statusContainer.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                }
                is RecipeListUiModel.Data -> {
                    adapter.submitList(it.recipes)
                    binding.statusContainer.visibility = View.GONE
                    binding.recipesRecycleView.adapter = adapter
                }
                RecipeListUiModel.Error -> {
                    binding.statusContainer.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }
            }
        }
    }

    companion object {
        private const val SHOW_ONLY_SAVED_FLAG = "SHOW_ONLY_SAVED_FLAG"
        fun newInstance(showOnlySaved: Boolean): RecipeListFragment {
            val recipeListFragment = RecipeListFragment()
                recipeListFragment.arguments = bundleOf(
                    SHOW_ONLY_SAVED_FLAG to showOnlySaved
                )
            return recipeListFragment
            }
        }
    }