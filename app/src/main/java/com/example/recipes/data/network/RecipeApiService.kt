package com.example.recipes.data.network

import com.example.recipes.BuildConfig.API_HOST
import com.example.recipes.BuildConfig.API_KEY
import com.example.recipes.data.network.dto.RandomRecipeDto
import com.example.recipes.data.network.dto.RecipeDto
import com.example.recipes.data.network.dto.RecipeListDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://${API_HOST}/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RecipeApiService {

    @GET("recipes/{id}/information")
    suspend fun getRecipe(
        @Path("id") id: Int,
        @Header("X-RapidAPI-Key") key: String = API_KEY,
        @Header("X-RapidAPI-Host") host: String = API_HOST
    ): RecipeDto

    @GET("recipes/random")
    suspend fun getRandomRecipe(
        @Header("X-RapidAPI-Key") key: String = API_KEY,
        @Header("X-RapidAPI-Host") host: String = API_HOST,
        @Query("number") itemsNumber: Int = 5
    ): RandomRecipeDto

    @GET("recipes/complexSearch")
    suspend fun getRecipeByRequest(
        @Header("X-RapidAPI-Key") key: String = API_KEY,
        @Header("X-RapidAPI-Host") host: String = API_HOST,
        @Query("query") title: String,
        @Query("number") itemsNumber: Int = 5
    ): RecipeListDto
}

object RecipeApi {
    val retrofitService: RecipeApiService by lazy { retrofit.create(RecipeApiService::class.java) }
}
