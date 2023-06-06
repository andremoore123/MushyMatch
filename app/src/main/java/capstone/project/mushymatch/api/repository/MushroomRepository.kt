package capstone.project.mushymatch.api.repository

import android.util.Log
import capstone.project.mushymatch.api.ApiService
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponse
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MushroomRepository(private val apiService: ApiService) {
    fun getMushrooms(callback: ApiCallback<List<GetMushroomResponseItem>>) {
        val call = apiService.getMushrooms()
        call.enqueue(object : Callback<List<GetMushroomResponseItem>> {
            override fun onResponse(
                call: Call<List<GetMushroomResponseItem>>,
                response: Response<List<GetMushroomResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val recipesResponse = response.body()
                    recipesResponse?.let { callback.onSuccess(it) }
                    Log.d("MushroomRepository", "Success to fetch recipes")
                } else {
                    callback.onError("Failed to fetch recipes")
                    Log.d("MushroomRepository", "Failed to fetch recipes")
                }
            }

            override fun onFailure(call: Call<List<GetMushroomResponseItem>>, t: Throwable) {
                callback.onError(t.message ?: "Error occurred")
            }
        })
    }

    fun getMushroomDetail(id: Int, callback: ApiCallback<DetailMushroomResponse>) {
        val call = apiService.getMushroomDetail(id)
        call.enqueue(object : Callback<DetailMushroomResponse> {
            override fun onResponse(
                call: Call<DetailMushroomResponse>,
                response: Response<DetailMushroomResponse>
            ) {
                if (response.isSuccessful) {
                    val detailResponse = response.body()
                    detailResponse?.let { callback.onSuccess(it) }
                } else {
                    callback.onError("Failed to fetch mushroom detail")
                }
            }

            override fun onFailure(call: Call<DetailMushroomResponse>, t: Throwable) {
                callback.onError(t.message ?: "Error occurred")
            }
        })
    }

    fun getRecipes(id: Int, callback: ApiCallback<List<ListRecipesResponseItem>>) {
        val call = apiService.getRecipes(id)
        call.enqueue(object : Callback<List<ListRecipesResponseItem>> {
            override fun onResponse(
                call: Call<List<ListRecipesResponseItem>>,
                response: Response<List<ListRecipesResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val recipesResponse = response.body()
                    recipesResponse?.let { callback.onSuccess(it) }
                    Log.d("MushroomRepository", "Success to fetch recipes")
                } else {
                    callback.onError("Failed to fetch recipes")
                    Log.d("MushroomRepository", "Failed to fetch recipes")
                }
            }

            override fun onFailure(call: Call<List<ListRecipesResponseItem>>, t: Throwable) {
                callback.onError(t.message ?: "Error occurred")
            }
        })
    }



    fun getRecipeDetail(id: Int, callback: ApiCallback<DetailRecipesResponse>) {
        val call = apiService.getRecipeDetail(id)
        call.enqueue(object : Callback<DetailRecipesResponse> {
            override fun onResponse(
                call: Call<DetailRecipesResponse>,
                response: Response<DetailRecipesResponse>
            ) {
                if (response.isSuccessful) {
                    val detailResponse = response.body()
                    detailResponse?.let { callback.onSuccess(it) }
                } else {
                    callback.onError("Failed to fetch recipe detail")
                }
            }

            override fun onFailure(call: Call<DetailRecipesResponse>, t: Throwable) {
                callback.onError(t.message ?: "Error occurred")
            }
        })
    }
}

interface ApiCallback<T> {
    fun onSuccess(response: T)
    fun onError(errorMessage: String)
}
