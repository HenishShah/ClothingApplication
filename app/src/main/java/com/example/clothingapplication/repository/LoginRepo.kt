package com.example.clothingapplication.repository

import com.example.clothingapplication.utils.DataState
import com.example.clothingapplication.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object LoginRepo {

    suspend fun loginValidation(
        userName: String,
        password: String,
    ): Flow<DataState<String>> =
        flow {
            if (userName.trim().isEmpty()) emit(DataState.Error("Please Enter A User Name"))
            else if (password.trim().isEmpty()) emit(DataState.Error("Please Enter A Password"))
            else {
                try {
                    if (userName != "UserName" && password != "Password") emit(DataState.Error("Invalid User Name or Password"))
                    else emit(DataState.Success("Login Successfully..."))
                } catch (e: Exception) {
                    logD(
                        "loginResponseException",
                        "${e.message ?: e.localizedMessage ?: "Exception"}"
                    )
                    emit(DataState.Error(e.localizedMessage ?: "Exception"))
                }
            }
        }

}