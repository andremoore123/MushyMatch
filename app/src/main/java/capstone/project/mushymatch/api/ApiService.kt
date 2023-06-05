package capstone.project.mushymatch.api

import capstone.project.mushymatch.api.response.mushroom.*
import capstone.project.mushymatch.api.response.recipe.*
import capstone.project.mushymatch.api.response.mushroom.DetailMushroomResponse
import capstone.project.mushymatch.api.response.recipe.DetailRecipesResponse
import capstone.project.mushymatch.api.response.recipe.ListRecipesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
//    @GET("get-jamur")
//    fun getMushrooms(): Call<GetMushroomResponse>

    @GET("jamur/{id_jamur}")
    fun getMushroomDetail(@Path("id_jamur") id: Int): Call<DetailMushroomResponse>

    @GET("list-recipes/{id_jamur}")
    fun getRecipes(@Path("id_jamur") id: Int): Call<ListRecipesResponse>

    @GET("recipes/{id_recipe}")
    fun getRecipeDetail(@Path("id_recipe") id: Int): Call<DetailRecipesResponse>
}
