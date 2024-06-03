package com.example.clothingapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.clothingapplication.R

class PreferenceHelper(context: Context) {
    private val masterKeyAlias: String = createGetMasterKey()

    val sharedPreference: SharedPreferences = EncryptedSharedPreferences.create(
        context.resources.getString(R.string.app_name),
        masterKeyAlias,
        context.applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private fun createGetMasterKey(): String {
        return MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }
}