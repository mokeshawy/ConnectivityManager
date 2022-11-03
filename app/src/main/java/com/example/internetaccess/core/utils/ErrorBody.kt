package com.example.internetaccess.core.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response


data class ErrorBody(
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("errorTime")
    val errorTime: String,
    @SerializedName("errorCode")
    val errorCode: String
)

fun Response<*>.getErrorBody(): ErrorBody? =
    Gson().fromJson(errorBody()?.string(), ErrorBody::class.java)