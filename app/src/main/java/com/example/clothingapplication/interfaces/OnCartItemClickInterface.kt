package com.example.clothingapplication.interfaces

import com.example.clothingapplication.model.ProductModel

interface OnCartItemClickInterface {
    fun onItemClicked(productModel: ProductModel)
    fun onDeleteItemClicked(productModel: ProductModel)
}