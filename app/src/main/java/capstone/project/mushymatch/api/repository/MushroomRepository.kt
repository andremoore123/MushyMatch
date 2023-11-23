package capstone.project.mushymatch.api.repository

import capstone.project.mushymatch.api.ApiService
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponseItem

class MushroomRepository(private val apiService: ApiService): IMushroomRepository {
    override suspend fun getMushrooms(): Result<List<GetMushroomResponseItem>> {
        return try {
            val response = apiService.getMushrooms().execute()
            if (response.isSuccessful) {
                val data = response.body()
                Result.Success(data ?: emptyList())
            } else {
                Result.Error("Failed to fetch mushrooms")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    override suspend fun getMushroomDetail(id: Int): Result<DetailMushroomResponse> {
        return try {
            val response = apiService.getMushroomDetail(id).execute()

            if (response.isSuccessful) {
                val detailResponse = response.body()
                Result.Success(detailResponse ?: error("Response body is null"))
            } else {
                Result.Error("Failed to fetch mushroom detail")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    override suspend fun getRecipes(id: Int): Result<List<ListRecipesResponseItem>> = try {
        val response = apiService.getRecipes(id).execute()

        if (response.isSuccessful) {
            val recipesResponse = response.body()
            Result.Success(recipesResponse ?: error("Response body is null"))
        } else {
            Result.Error("Failed to fetch recipes")
        }
    } catch (e: Exception) {
        Result.Error("Error occurred: ${e.message}")
    }

    override suspend fun getRecipeDetail(id: Int): Result<DetailRecipesResponse> = try {
        val response = apiService.getRecipeDetail(id).execute()

        if (response.isSuccessful) {
            val detailResponse = response.body()
            Result.Success(detailResponse ?: error("Response body is null"))
        } else {
            Result.Error("Failed to fetch recipe detail")
        }
    } catch (e: Exception) {
        Result.Error("Error occurred: ${e.message}")
    }
}

interface IMushroomRepository {
    suspend fun getMushrooms(): Result<List<GetMushroomResponseItem>>
    suspend fun getMushroomDetail(id: Int): Result<DetailMushroomResponse>
    suspend fun getRecipes(id: Int): Result<List<ListRecipesResponseItem>>
    suspend fun getRecipeDetail(id: Int): Result<DetailRecipesResponse>
}

interface ApiCallback<T> {
    fun onSuccess(response: T)
    fun onError(errorMessage: String)
}

sealed class Result<T> {
    data class Success<T>(val response: T): Result<T>()
    data class Error<Nothing>(val message: String): Result<Nothing>()
}
