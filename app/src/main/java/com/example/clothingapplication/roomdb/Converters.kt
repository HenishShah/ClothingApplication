package com.example.clothingapplication.roomdb

import androidx.room.TypeConverter
import com.example.clothingapplication.model.RatingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromItemModels(itemList: RatingModel): String {
        return Gson().toJson(itemList)
    }

    @TypeConverter
    fun toItemModels(itemList: String): RatingModel {
        val itemModelList = object : TypeToken<RatingModel>() {}.type
        return Gson().fromJson(itemList, itemModelList)
    }
}