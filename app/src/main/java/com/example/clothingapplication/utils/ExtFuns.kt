package com.example.clothingapplication.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testapplication.utils.CheckConnectivity
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.disable() {
    this.isEnabled = false
}

fun View.enable() {
    this.isEnabled = true
}

fun Context.notifyUser(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.showError(
    message: String,
    positiveButtonText: String = "Yes",
    negativeButtonText: String = "No",
    positiveListener: DialogInterface.OnClickListener? = null,
    negativeListener: DialogInterface.OnClickListener? = null
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setCancelable(true)
        .setPositiveButton(positiveButtonText, positiveListener)
        .setNegativeButton(negativeButtonText, negativeListener)
        .show()
}

fun Context.showErrorPositive(
    message: String,
    positiveButtonText: String = "Ok",
    positiveListener: DialogInterface.OnClickListener? = null
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveButtonText, positiveListener)
        .show()
}

fun checkNetwork(activity: Activity): CheckConnectivity {
    val connectivityManager: ConnectivityManager =
        activity.applicationContext.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
    return CheckConnectivity.getInstance(connectivityManager)
}

fun checkNetworkInternetConnected(activity: Activity): Boolean {
    val connectivityManager: ConnectivityManager =
        activity.applicationContext.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
    return CheckConnectivity(connectivityManager).isNetworkAvailable()
}

//for logs
fun logD(tag: String = "ProductApp", message: String) {
    Log.d(tag, message)
}

fun logE(tag: String = "ProductApp", message: String) {
    Log.e(tag, message)
}

fun logI(tag: String = "ProductApp", message: String = "") {
    Log.i(tag, message)
}

object AppUtils{
    fun checkNetwork(activity: Activity): CheckConnectivity {
        val connectivityManager: ConnectivityManager =
            activity.applicationContext.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
        return CheckConnectivity.getInstance(connectivityManager)
    }

    fun checkNetworkInternetConnected(connectivityManager: ConnectivityManager): Boolean {
        return CheckConnectivity(connectivityManager).isNetworkAvailable()
    }
}