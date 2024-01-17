package com.example.ricknmorty.data.api

import com.example.ricknmorty.data.response.CharactersResponse
import com.example.ricknmorty.data.response.ResultsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/character")
    fun getCharacters(
        @Query("page") page: String
    ): Call<CharactersResponse>

    @GET("api/character")
    fun getCharacterByName(
        @Query("page") page: String,
        @Query("name") name: String
    ): Call<CharactersResponse>

    @GET("api/character/{id}")
    fun getCharacterById(
        @Path("id") id: String
    ): Call<ResultsItem>
}