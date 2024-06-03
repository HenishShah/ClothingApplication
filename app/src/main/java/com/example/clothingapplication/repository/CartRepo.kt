package com.example.clothingapplication.repository

import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.roomdb.ProductDao
import com.example.clothingapplication.utils.DataState
import com.example.clothingapplication.utils.logD
import com.example.clothingapplication.utils.logE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object CartRepo {

    suspend fun createCartDetails(
        productDao: ProductDao,
        productModel: ProductModel
    ): Flow<DataState<String>> = flow {
        try {
            productDao.insert(productModel)
            logD("CART_REPO_INSERT", "Successfully Inserted - $productModel")
            emit(DataState.Success("Data Inserted Successfully"))
        } catch (e: Exception) {
            logE("CART_REPO_INSERT_ERROR", "Error - ${e.message}")
            emit(DataState.Error("Something went wrong"))
        }
    }

    suspend fun getCartList(
        productDao: ProductDao
    ): Flow<DataState<List<ProductModel>>> = flow {
        try {
            productDao.getProductList().collect {
                logD("CART_REPO_VIEW", "List - $it")
                emit(DataState.Success(it))
            }
        } catch (e: Exception) {
            logE("CART_REPO_VIEW_ERROR", "Error - ${e.message}")
            emit(DataState.Error("Something went wrong"))
        }
    }

    suspend fun deleteCartDetails(
        productDao: ProductDao,
        productModel: ProductModel
    ): Flow<DataState<String>> = flow {
        try {
            productDao.delete(productModel)
            logD("CART_REPO_DELETE_ERROR", "Successfully Deleted - $productModel")
            emit(DataState.Success("Data Deleted Successfully"))
        } catch (e: Exception) {
            logE("CART_REPO_DELETE_ERROR", "Error - ${e.message}")
            emit(DataState.Error("Something went wrong"))
        }
    }

}