package capstone.project.mushymatch.api.repository

import capstone.project.mushymatch.api.ApiService
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponse
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MushroomRepository(private val apiService: ApiService) {

    fun getMushrooms(callback: ApiCallback<GetMushroomResponse>) {
        val call = apiService.getMushrooms()
        call.enqueue(object : Callback<GetMushroomResponse> {
            override fun onResponse(
                call: Call<GetMushroomResponse>,
                response: Response<GetMushroomResponse>
            ) {
                if (response.isSuccessful) {
                    val mushroomResponse = response.body()
                    mushroomResponse?.let { callback.onSuccess(it) }
                } else {
                    callback.onError("Failed to fetch mushrooms")
                }
            }

            override fun onFailure(call: Call<GetMushroomResponse>, t: Throwable) {
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

    fun getRecipes(id: Int, callback: ApiCallback<ListRecipesResponse>) {
        val call = apiService.getRecipes(id)
        call.enqueue(object : Callback<ListRecipesResponse> {
            override fun onResponse(
                call: Call<ListRecipesResponse>,
                response: Response<ListRecipesResponse>
            ) {
                if (response.isSuccessful) {
                    val recipesResponse = response.body()
                    recipesResponse?.let { callback.onSuccess(it) }
                } else {
                    callback.onError("Failed to fetch recipes")
                }
            }

            override fun onFailure(call: Call<ListRecipesResponse>, t: Throwable) {
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
