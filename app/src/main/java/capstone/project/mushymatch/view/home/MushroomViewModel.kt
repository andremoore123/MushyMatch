package capstone.project.mushymatch.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.ApiService
import capstone.project.mushymatch.api.repository.ApiCallback
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem

class MushroomViewModel(private val repository: MushroomRepository) : ViewModel() {

    private val _mushrooms = MutableLiveData<List<GetMushroomResponseItem>>()
    val mushrooms: LiveData<List<GetMushroomResponseItem>> = _mushrooms

    fun loadMushrooms() {
        repository.getMushrooms(object : ApiCallback<List<GetMushroomResponseItem>> {
            override fun onSuccess(response: List<GetMushroomResponseItem>) {
                _mushrooms.value = response
            }

            override fun onError(errorMessage: String) {
                // Handle error
                Log.e("MushroomViewModel", errorMessage)
            }
        })
    }
}

class MushroomViewModelFactory(private val repository: MushroomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MushroomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MushroomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

