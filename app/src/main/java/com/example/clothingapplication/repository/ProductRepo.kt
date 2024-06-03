package com.example.clothingapplication.repository

import com.example.clothingapplication.model.CategoryModel
import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.retrofitApi.RetrofitInstance
import com.example.clothingapplication.utils.DataState
import com.example.clothingapplication.utils.logD
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray

object ProductRepo {

    suspend fun getProductsList(): Flow<DataState<List<ProductModel>>> = flow {
        try {
            val apiResponse = RetrofitInstance.api.getProductsApi()
            logD("ProductApiResponse", "body - ${apiResponse.body()}, code - ${apiResponse.code()}")
            if (apiResponse.isSuccessful) {
                val response = apiResponse.body()!!

                logD("ProductApiResponseIN", "body - ${apiResponse.body()}, code - ${apiResponse.code()}")
                val listType = object : TypeToken<List<ProductModel>>() {}.type
                val productModelList = Gson().fromJson<List<ProductModel>>(response, listType)

                emit(DataState.Success(productModelList))
            } else {
                emit(DataState.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {
            logD(
                "ProductApiError",
                "body - ${e.message}}"
            )
            emit(DataState.Error("Something went wrong"))
        }
    }

    suspend fun getCategoryList(): Flow<DataState<List<CategoryModel>>> = flow {
        try {
            val apiResponse = RetrofitInstance.api.getCategoriesApi()
            logD(
                "CategoryApiResponse",
                "body - ${apiResponse.body()}, code - ${apiResponse.code()}"
            )
            if (apiResponse.isSuccessful) {
                val response = apiResponse.body()!!

                val jsonArray = JSONArray(response)
                logD("CategoryApiResponseIN", "jsonArray - $jsonArray")

                val categoryModelList: ArrayList<CategoryModel> = ArrayList()
                categoryModelList.add(CategoryModel("All", true))
                for (i in 0 until  jsonArray.length()){
                    categoryModelList.add(CategoryModel(jsonArray[i].toString()))
                }

                emit(DataState.Success(categoryModelList))
            } else {
                emit(DataState.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {
            logD(
                "CategoryApiError",
                "body - ${e.message}}"
            )
            emit(DataState.Error("Something went wrong"))
        }
    }
}