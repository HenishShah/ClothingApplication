package com.example.clothingapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothingapplication.repository.LoginRepo
import com.example.clothingapplication.utils.DataState
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _dataState: MutableLiveData<DataState<String>> =
        MutableLiveData()


    val dataState: LiveData<DataState<String>>
        get() = _dataState

    fun setStateEvent(
        userName: String,
        password: String,
    ) {
        _dataState.value = DataState.Loading
        viewModelScope.launch {
            LoginRepo.loginValidation(
                userName, password
            )
                .collect { dataState ->
                    _dataState.value = dataState
                }
        }
    }

}