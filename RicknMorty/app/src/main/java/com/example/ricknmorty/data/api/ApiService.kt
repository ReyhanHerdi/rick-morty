package com.example.ricknmorty.data.api

import com.example.ricknmorty.data.response.CharactersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/character")
    fun getCharacters(
        @Query("page") page: String
    ): Call<CharactersResponse>
}