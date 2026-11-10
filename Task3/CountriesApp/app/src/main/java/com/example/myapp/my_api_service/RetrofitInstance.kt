package com.example.myapp.my_api_service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ICountriesAPI by lazy{
        Retrofit.Builder()
            .baseUrl("https://countries.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ICountriesAPI::class.java)
    }
}
