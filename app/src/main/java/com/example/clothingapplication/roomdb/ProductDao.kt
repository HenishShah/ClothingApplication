package com.example.clothingapplication.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clothingapplication.model.ProductModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productModel: ProductModel)

    @Query("SELECT * FROM product_table")
    fun getProductList(): Flow<List<ProductModel>>

    @Delete
    suspend fun delete(productModel: ProductModel)
}