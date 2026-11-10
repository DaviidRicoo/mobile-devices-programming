package com.example.myapp.my_api_service

import com.example.myapp.mymodel.Country
import retrofit2.http.GET

interface ICountriesAPI {
    @GET("countries")
    suspend fun getCountries(): List<Country>
}