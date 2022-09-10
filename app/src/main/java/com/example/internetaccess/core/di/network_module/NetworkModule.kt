package com.example.internetaccess.core.di.network_module

import com.example.internetaccess.BuildConfig
import com.example.internetaccess.core.utils.LiveOkhttpLoggingInterceptor
import com.example.internetaccess.services.InternetServices
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "http://ec2-157-175-229-147.me-south-1.compute.amazonaws.com:8080/"
const val TOKEN =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSX0xJU1QiLCJVU0VSX1ZJRVciLCJVU0VSX1VQREFURSIsIlVTRVJfREVMRVRFIiwiVVNFUl9DUkVBVEUiLCJNRVJDSEFOVF9MSVNUIiwiTUVSQ0hBTlRfVklFVyIsIlNJVEVfTElTVF9BTEwiLCJTSVRFX0xJU1QiLCJTSVRFX1ZJRVciLCJTSVRFX1VQREFURSIsIlNJVEVfREVMRVRFIiwiU0lURV9DUkVBVEUiLCJDT1VOVFJZX0xJU1QiLCJDT1VOVFJZX1ZJRVciLCJDSVRZX0xJU1QiLCJDSVRZX1ZJRVciLCJaT05FX0xJU1QiLCJaT05FX1ZJRVciLCJCQU5LX0xJU1QiLCJCQU5LX1ZJRVciLCJORkNfTElTVCIsIk5GQ19WSUVXIiwiTkZDX1VQREFURSIsIk5GQ19DUkVBVEUiLCJDT1JQT1JBVEVfTElTVCIsIkNPUlBPUkFURV9WSUVXIiwiQ09SUE9SQVRFX0NPTlRBQ1RfTElTVCIsIkNPUlBPUkFURV9DT05UQUNUX1ZJRVciLCJQUk9EVUNUX0NBVEVHT1JZX0xJU1QiLCJQUk9EVUNUX0NBVEVHT1JZX1ZJRVciLCJPVFVfTElTVCIsIk9UVV9WSUVXIiwiT1RVX1VQREFURSIsIk9UVV9DUkVBVEUiLCJUUkFOU0FDVElPTl9MSVNUX0FMTCIsIlRSQU5TQUNUSU9OX1ZJRVciLCJUUkFOU0FDVElPTl9MSVNUIiwiVFJBTlNBQ1RJT05fTUFLRSIsIk1BU1RFUl9NRVJDSEFOVF9MSVNUIiwiTUFTVEVSX01FUkNIQU5UX1ZJRVciLCJBU1NFVF9MSVNUIiwiQVNTRVRfVklFVyIsIkNPUlBPUkFURV9CSUxMSU5HX0FDQ09VTlRfTElTVCIsIkNPUlBPUkFURV9CSUxMSU5HX0FDQ09VTlRfVklFVyIsIlBST0RVQ1RfTElTVCIsIlBST0RVQ1RfVklFVyIsIlBST0RVQ1RfVVBEQVRFIiwiUFJPRFVDVF9ERUxFVEUiLCJQUk9EVUNUX0NSRUFURSIsIlBPTElDWV9MSVNUIiwiUE9MSUNZX1ZJRVciLCJTSElGVF9MSVNUIiwiU0hJRlRfVklFVyIsIlNISUZUX1VQREFURSIsIlNISUZUX0NSRUFURSIsIk1FUkNIQU5UX0NPTlRBQ1RfTElTVCIsIk1FUkNIQU5UX0NPTlRBQ1RfVklFVyIsIk1FUkNIQU5UX0NPTlRBQ1RfVVBEQVRFIiwiTUVSQ0hBTlRfQ09OVEFDVF9DUkVBVEUiLCJNRVJDSEFOVF9CQU5LX0FDQ09VTlRfTElTVCIsIk1FUkNIQU5UX0JBTktfQUNDT1VOVF9WSUVXIiwiTUVSQ0hBTlRfQkFOS19BQ0NPVU5UX1VQREFURSIsIk1FUkNIQU5UX0JBTktfQUNDT1VOVF9ERUxFVEUiLCJNRVJDSEFOVF9CQU5LX0FDQ09VTlRfQ1JFQVRFIiwiSU5WT0lDRV9MSVNUX0FMTCIsIklOVk9JQ0VfTElTVCIsIklOVk9JQ0VfVklFVyIsIklOVk9JQ0VfVVBEQVRFIiwiSU5WT0lDRV9DUkVBVEUiLCJEQVNIQk9BUkRfTElTVCIsIkRBU0hCT0FSRF9WSUVXIiwiU0FMRVNfTElTVCIsIlNBTEVTX1ZJRVciLCJJTlZPSUNFX1BBQ0tBR0VfTElTVCIsIklOVk9JQ0VfUEFDS0FHRV9WSUVXIiwiSU5WT0lDRV9QQUNLQUdFX1VQREFURSIsIklOVk9JQ0VfUEFDS0FHRV9DUkVBVEUiLCJNRVJDSEFOVF9ET0NVTUVOVF9MSVNUIiwiTUVSQ0hBTlRfRE9DVU1FTlRfVklFVyIsIk1JTEVBR0VfSU1BR0VfTElTVCIsIk1JTEVBR0VfSU1BR0VfVklFVyIsIk1FUkNIQU5UX1VTRVJfTElTVCIsIk1FUkNIQU5UX1VTRVJfVklFVyIsIk1FUkNIQU5UX1VTRVJfVVBEQVRFIiwiTUVSQ0hBTlRfVVNFUl9DUkVBVEUiLCJDT1JQT1JBVEVfVVNFUl9MSVNUIiwiQ09SUE9SQVRFX1VTRVJfVklFVyIsIk1FUkNIQU5UX0JJTExJTkdfQUNDT1VOVF9WSUVXIiwiTUVSQ0hBTlRfRGVwb3NpdF9MSVNUIl0sImlzcyI6IlNTUyIsInVzZXJJZCI6MSwicmVsYXRlZFN5c3RlbUlkIjoxLCJsb2NhbE5hbWUiOiJhZG1pbiIsIm5iZiI6MTY2MjU2MjM5OCwidXNlckNyZWRlbnRpYWxJZCI6MiwiZW5OYW1lIjoiYWRtaW4iLCJzaXRlSWQiOjEsInVzZXJUeXBlIjoiTUVSQ0hBTlQiLCJleHAiOjE2NjI1ODM5OTgsImlhdCI6MTY2MjU2MjM5OCwianRpIjoiNDdjZjVjNGUtODVlYy00ZmExLTllMDMtMTYzNmFhOTc5OTI4In0.O5i47XWStz7SDKt6v53QlNSM_0OL7TNZCvNky98hmVc1MZr-9Hdj2IMRCMSJo4tLVBKHTYGr2vcvf-ZtEDBgpfk7els8BdZtaeliN4xmDxmRzRnB9J28SuXraquRzhPfuJZ_h-mmP6bR-6mju7KBzHHpnK4BTvyNBt0__BGtVl_P9edxqKlY4LdB7Ylk7Hk3edjnPUC3hCYBk0PjMzb0lJwqjDTh24U2lSSdawWu3-QwRVEzE8JXI9fDaB4b3FbWb0-rk1bWZh2Yx1feS7uTjVyE3AYhmvovMvAMyPjurVBlBV8EaF7AirsujAuwufbii_kcx7xjDkcg5IS5V9uGdA"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideInternetService(retrofit: Retrofit): InternetServices =
        retrofit.create(InternetServices::class.java)

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Provides
    fun provideConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Named("AuthInterceptor")
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val newBuilder = chain.request()
            .newBuilder()
        newBuilder.addHeader("Authorization", "Bearer $TOKEN")
        newBuilder.build().let { chain.proceed(it) }
    }


    @Provides
    @Named("LoggingInterceptor")
    @Singleton
    fun httpLoggingInterceptor(): Interceptor {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor { message ->
                Timber.tag("OkHttp").d(message)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        else
            return LiveOkhttpLoggingInterceptor { message ->
                Timber.tag("OKHttp").d(message)

            }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Singleton
    @Provides
    fun client(
        @Named("AuthInterceptor")
        authInterceptor: Interceptor,
        @Named("LoggingInterceptor")
        loggingInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient().newBuilder().run {
            addInterceptor(authInterceptor)
            addInterceptor(loggingInterceptor)
            connectTimeout(2, TimeUnit.MINUTES)
            readTimeout(2, TimeUnit.MINUTES)
            build()
        }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(converterFactory)
        .client(okHttpClient)
        .build()
}


