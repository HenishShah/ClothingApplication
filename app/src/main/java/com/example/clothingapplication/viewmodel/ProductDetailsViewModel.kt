package com.example.clothingapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.repository.CartRepo
import com.example.clothingapplication.roomdb.ProductDao
import com.example.clothingapplication.utils.DataState
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    private val _insertDataState: MutableLiveData<DataState<String>> = MutableLiveData()

    val insertDataState: LiveData<DataState<String>>
        get() = _insertDataState

    fun setInsertDataStateEvent(
        productDao: ProductDao,
        productModel: ProductModel
    ) {
        _insertDataState.value = DataState.Loading
        viewModelScope.launch {
            CartRepo.createCartDetails(productDao, productModel)
                .collect { dataState -> _insertDataState.value = dataState }
        }
    }
}