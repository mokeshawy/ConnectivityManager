package com.example.internetaccess.services

import com.example.internetaccess.feature.mainactivity.data.model.reponse.corporate_response.CorporateResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InternetServices {

    @GET("corporate-service/corporate/{id}")
    suspend fun retrieveCorporate(@Path("id") id : Long) : Response<CorporateResponseDto>
}