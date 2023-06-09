package capstone.project.mushymatch.view.recipes.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.repository.ApiCallback
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse

class CookingRecipesViewModel(private val mushroomRepository: MushroomRepository) : ViewModel() {
    private val _recipeDetail = MutableLiveData<DetailRecipesResponse>()
    val recipeDetail: LiveData<DetailRecipesResponse> = _recipeDetail

    fun loadRecipeDetail(recipeId: Int) {
        mushroomRepository.getRecipeDetail(recipeId, object : ApiCallback<DetailRecipesResponse> {
            override fun onSuccess(response: DetailRecipesResponse) {
                _recipeDetail.value = response
            }

            override fun onError(errorMessage: String) {
                // Tangani kesalahan jika diperlukan
            }
        })
    }
}

class CookingRecipesViewModelFactory(private val mushroomRepository: MushroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookingRecipesViewModel::class.java)) {
            return CookingRecipesViewModel(mushroomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

