package com.example.recipes.presentation.recipeList

import android.util.Log
import androidx.lifecycle.*
import com.example.recipes.domain.Repository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class RecipeListViewModel(private val repository: Repository, private val showOnlySaved: Boolean) : ViewModel() {

    private val _state = MutableLiveData<RecipeListUiModel>()
    val state: LiveData<RecipeListUiModel> = _state

    private val searchQuery = MutableStateFlow("")

    private val queryFlow = searchQuery.debounce(400L).distinctUntilChanged()

    init {
        getRecipe()
        collectQueryFlow()
    }

    fun setTitle(title: String) {
        searchQuery.value = title
    }

     fun getRecipe() {
        viewModelScope.launch {
            _state.value = RecipeListUiModel.Loading
            try {
                _state.value = RecipeListUiModel.Data(
                    repository.getRandomRecipe(showOnlySaved)
                )
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "", e)
                _state.value = RecipeListUiModel.Error
            }
        }
    }

    private fun collectQueryFlow() {
        viewModelScope.launch {
            queryFlow.collectLatest {
                if (it.length > 2) {
                    _state.value = RecipeListUiModel.Loading
                    try {
                        _state.value = RecipeListUiModel.Data(
                            repository.getRecipeByRequest(it, showOnlySaved)
                        )
                    } catch (e: Exception) {
                        Log.e("RecipeViewModel", "", e)
                        _state.value = RecipeListUiModel.Error
                    }
                } else if (it.isEmpty()) {
                    getRecipe()
                }
            }

        }
    }


    class RecipeListFactory(private val repository: Repository, private val fragmentFlag: Boolean) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeListViewModel(repository, fragmentFlag) as T

        }
    }
}