package com.example.ricknmorty.data.repository

import com.example.ricknmorty.data.api.ApiConfig
import com.example.ricknmorty.data.response.CharactersResponse
import retrofit2.Call

class CharacterRepository {
    suspend fun getCharacter(page: String): Call<CharactersResponse> {
        val apiService = ApiConfig.getApiService()
        return apiService.getCharacters(page)
    }

    companion object {
        @Volatile
        private var instance: CharacterRepository? = null

        fun getInstance() : CharacterRepository =
            instance ?: synchronized(this) {
                instance ?: CharacterRepository()
                    .also {
                        instance = it
                    }
            }
    }
}