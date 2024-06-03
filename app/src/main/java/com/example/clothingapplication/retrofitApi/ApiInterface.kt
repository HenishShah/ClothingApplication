package com.example.clothingapplication.retrofitApi

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("products")
    suspend fun getProductsApi(): Response<String>

    @GET("products/categories")
    suspend fun getCategoriesApi(): Response<String>

}