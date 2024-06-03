package com.example.clothingapplication.retrofitApi

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class StringConverterFactory : Converter.Factory() {
    private val MEDIA_TYPE: MediaType = "text/plain".toMediaTypeOrNull()!!

    fun create(): StringConverterFactory {
        return StringConverterFactory()
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody?, *>? {
        return if (String::class.java == type) {
            Converter { value -> value?.string() }
        } else null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation?>?,
        methodAnnotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<String?, RequestBody?>? {
        return if (String::class.java == type) {
            Converter<String?, RequestBody?> { value -> value?.toRequestBody(MEDIA_TYPE) }
        } else null
    }
}