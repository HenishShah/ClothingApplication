package com.example.clothingapplication.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.clothingapplication.model.CategoryModel
import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.repository.ProductRepo
import com.example.clothingapplication.utils.AppUtils
import com.example.clothingapplication.utils.DataState
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val _dataState: MutableLiveData<DataState<List<ProductModel>>> = MutableLiveData()
    private val _dataStateCategory: MutableLiveData<DataState<List<CategoryModel>>> = MutableLiveData()

    init {
        val connectivityManager: ConnectivityManager =
            application.applicationContext.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (AppUtils.checkNetworkInternetConnected(connectivityManager)) {
            setDataStateCategoryEvent()
            setDataStateEvent()
        }
    }

    val dataState: LiveData<DataState<List<ProductModel>>>
        get() = _dataState

    val dataStateCategory: LiveData<DataState<List<CategoryModel>>>
        get() = _dataStateCategory

    private fun setDataStateEvent() {
        _dataState.value = DataState.Loading
        viewModelScope.launch {
            ProductRepo.getProductsList()
                .collect { dataState -> _dataState.value = dataState }
        }
    }

    private fun setDataStateCategoryEvent() {
        _dataStateCategory.value = DataState.Loading
        viewModelScope.launch {
            ProductRepo.getCategoryList()
                .collect { dataState -> _dataStateCategory.value = dataState }
        }
    }
}