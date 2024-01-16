package com.example.ricknmorty.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val retrofit = Retrofit
                .Builder()
                .baseUrl("https://rickandmortyapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return  retrofit.create(ApiService::class.java)
        }
    }
}