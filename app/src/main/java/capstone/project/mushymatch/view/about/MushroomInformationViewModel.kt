package capstone.project.mushymatch.view.about

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.repository.ApiCallback
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse

class MushroomInformationViewModel(private val mushroomRepository: MushroomRepository) : ViewModel() {

    private val _mushroomDetail = MutableLiveData<DetailMushroomResponse>()
    val mushroomDetail: LiveData<DetailMushroomResponse> = _mushroomDetail

    private val _recipes = MutableLiveData<ListRecipesResponse>()
    val recipes: LiveData<ListRecipesResponse> = _recipes

    fun getMushroomDetail(mushroomId: Int) {
        mushroomRepository.getMushroomDetail(mushroomId, object : ApiCallback<DetailMushroomResponse> {
            override fun onSuccess(response: DetailMushroomResponse) {
                _mushroomDetail.value = response

            }

            override fun onError(errorMessage: String) {
                // Tangani kesalahan pemanggilan API
            }
        })
    }

}

class MushroomInformationViewModelFactory(private val mushroomRepository: MushroomRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MushroomInformationViewModel::class.java)) {
            return MushroomInformationViewModel(mushroomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


