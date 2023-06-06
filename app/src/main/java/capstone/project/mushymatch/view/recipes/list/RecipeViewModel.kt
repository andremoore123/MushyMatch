package capstone.project.mushymatch.view.recipes.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.repository.ApiCallback
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem

class RecipeViewModel(private val repository: MushroomRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<ListRecipesResponseItem>>()
    val recipes = _recipes

    fun loadRecipes(idJamur: Int) {
        repository.getRecipes(idJamur, object : ApiCallback<List<ListRecipesResponseItem>> {
            override fun onSuccess(response: List<ListRecipesResponseItem>) {
                _recipes.value = response
            }

            override fun onError(errorMessage: String) {
                // Handle error
                Log.e("RecipeViewModel", errorMessage)
            }
        })
    }

}

class RecipeViewModelFactory(private val repository: MushroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}