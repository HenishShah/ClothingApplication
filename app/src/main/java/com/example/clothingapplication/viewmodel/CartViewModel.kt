package com.example.clothingapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.repository.CartRepo
import com.example.clothingapplication.roomdb.ProductDao
import com.example.clothingapplication.roomdb.ProductDatabase
import com.example.clothingapplication.utils.DataState
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val _insertDataState: MutableLiveData<DataState<String>> = MutableLiveData()
    private val _getCartListDataState: MutableLiveData<DataState<List<ProductModel>>> = MutableLiveData()
    private val _deleteDataState: MutableLiveData<DataState<String>> = MutableLiveData()

    init {
        val dao = ProductDatabase.invoke(application.applicationContext).getProductDao()
        setCartListDataStateEvent(dao)
    }

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

    val getCartListDataState: LiveData<DataState<List<ProductModel>>>
        get() = _getCartListDataState

    fun setCartListDataStateEvent(
        productDao: ProductDao
    ) {
        _getCartListDataState.value = DataState.Loading
        viewModelScope.launch {
            CartRepo.getCartList(productDao)
                .collect { dataState -> _getCartListDataState.value = dataState }
        }
    }

    val deleteDataState: LiveData<DataState<String>>
        get() = _deleteDataState

    fun setDeleteDataStateEvent(
        productDao: ProductDao,
        productModel: ProductModel
    ) {
        _deleteDataState.value = DataState.Loading
        viewModelScope.launch {
            CartRepo.deleteCartDetails(productDao, productModel)
                .collect { dataState -> _deleteDataState.value = dataState }
        }
    }
}