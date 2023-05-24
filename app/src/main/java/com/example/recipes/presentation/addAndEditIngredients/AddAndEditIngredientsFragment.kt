package com.example.recipes.presentation.addAndEditIngredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipes.R
import com.example.recipes.databinding.AddAndEditIngredientsFragmentBinding
import com.example.recipes.domain.models.Ingredient


class AddAndEditIngredientsFragment: Fragment() {
    private lateinit var binding: AddAndEditIngredientsFragmentBinding
    private val viewModel: AddAndEditIngredientsViewModel by viewModels{
        AddAndEditIngredientsViewModel.AddAndEditIngredientsFactory(
        requireArguments().getParcelable(INGREDIENT_ID_KEY))
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = AddAndEditIngredientsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.ingredientAmount.addTextChangedListener {
            viewModel.setAmount(it.toString())
        }
        viewModel.amount.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.ingredientAmount.error = getString(R.string.fill_field)
            }
            if(it.value != binding.ingredientAmount.text.toString()) {
                binding.ingredientAmount.setText(it.value)
            }
        }
        binding.ingredientName.addTextChangedListener {
            viewModel.setName(it.toString())
        }
        viewModel.name.observe(viewLifecycleOwner) {
            if (it.showError) {
                binding.ingredientName.error = getString(R.string.fill_field)
            }
            if(it.value != binding.ingredientName.text.toString()) {
                binding.ingredientName.setText(it.value)
            }
        }
        viewModel.unit.observe(viewLifecycleOwner) {
            binding.ingredientUnit.addTextChangedListener {
                viewModel.setUnit(it.toString())
            }
            if(it.value != binding.ingredientUnit.text.toString()) {
                binding.ingredientUnit.setText(it.value)
            }
        }


        binding.saveAction.setOnClickListener {
            viewModel.validateAndSaveIngredients()
        }
        viewModel.addedIngredient.observe(viewLifecycleOwner) {
            submitResult(ADDED_INGREDIENT_REQUEST_KEY, it)
        }
        viewModel.editedIngredient.observe(viewLifecycleOwner) {
            submitResult(EDITED_INGREDIENT_REQUEST_KEY, it)
        }


    }

    private fun submitResult(key: String, ingredient: Ingredient) {
        parentFragmentManager.setFragmentResult(
            key, bundleOf(
                INGREDIENT_BUNDLE_KEY to ingredient
            )
        )
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        const val ADDED_INGREDIENT_REQUEST_KEY = "ADDED_INGREDIENT_REQUEST_KEY"
        const val EDITED_INGREDIENT_REQUEST_KEY = "EDITED_INGREDIENT_REQUEST_KEY"
        const val INGREDIENT_BUNDLE_KEY = "INGREDIENT_BUNDLE_KEY"
        private const val INGREDIENT_ID_KEY = "INGREDIENT_ID_KEY"
        fun newInstance(ingredient: Ingredient?): AddAndEditIngredientsFragment {
           val addAndEditIngredientsFragment = AddAndEditIngredientsFragment()
            addAndEditIngredientsFragment.arguments = bundleOf(
                INGREDIENT_ID_KEY to ingredient
            )
            return addAndEditIngredientsFragment
        }
    }
}