package capstone.project.mushymatch.api

import capstone.project.mushymatch.api.response.PredictImageResponse
import capstone.project.mushymatch.api.response.mushroom.*
import capstone.project.mushymatch.api.response.recipe.*
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("get-jamur")
    fun getMushrooms(): Call<List<GetMushroomResponseItem>>

    @GET("jamur/{id_jamur}")
    fun getMushroomDetail(@Path("id_jamur") id: Int): Call<DetailMushroomResponse>

    @GET("list-recipes/{id_jamur}")
    fun getRecipes(@Path("id_jamur") id: Int): Call<List<ListRecipesResponseItem>>

    @GET("recipes/{id_recipe}")
    fun getRecipeDetail(@Path("id_recipe") id: Int): Call<DetailRecipesResponse>

    @Multipart
    @POST("predict/image")
    fun predictImage(@Part image: MultipartBody.Part): Call<PredictImageResponse>

}
