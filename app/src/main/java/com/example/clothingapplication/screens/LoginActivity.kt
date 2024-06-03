package com.example.clothingapplication.screens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.clothingapplication.databinding.ActivityLoginBinding
import com.example.clothingapplication.utils.DataState
import com.example.clothingapplication.utils.PreferenceHelper
import com.example.clothingapplication.utils.notifyUser
import com.example.clothingapplication.viewmodel.LoginViewModel
import com.example.clothingapplication.utils.constants.SharedPreferenceConsts

class LoginActivity : BaseActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initialize()
        setObservers()
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        sharedPref = PreferenceHelper(this).sharedPreference

        binding.apply {
            if (sharedPref.getBoolean(SharedPreferenceConsts.IS_USER_LOGGED_IN, false)){
                navToHomeScreen()
            }
            btSignIn.setOnClickListener {
                val userName = etUserName.text.toString()
                val password = etPassword.text.toString()

                viewModel.setStateEvent(userName, password)
            }
        }
    }

    private fun setObservers(){
        viewModel.dataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    notifyUser(dataState.data)
                    navToHomeScreen()
                }

                is DataState.Error -> {
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {
                    //show loader
                }
            }
        }
    }

    private fun navToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
        sharedPref.edit().putBoolean(SharedPreferenceConsts.IS_USER_LOGGED_IN, true).apply()
        startActivity(intent)
        finish()
    }
}