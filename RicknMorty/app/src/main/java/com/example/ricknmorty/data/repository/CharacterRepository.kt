package com.example.ricknmorty.data.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.ricknmorty.data.api.ApiConfig
import com.example.ricknmorty.data.response.CharactersResponse
import com.example.ricknmorty.data.response.ResultsItem
import com.example.ricknmorty.data.room.Favourite
import com.example.ricknmorty.data.room.FavouriteDao
import com.example.ricknmorty.data.room.FavouriteDatabase
import retrofit2.Call
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CharacterRepository(context: Context) {
    private val favouriteDao: FavouriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavouriteDatabase.getDatabase(context)
        favouriteDao = db.favouriteDao()
    }
    suspend fun getCharacter(page: String): Call<CharactersResponse> {
        val apiService = ApiConfig.getApiService()
        return apiService.getCharacters(page)
    }

    suspend fun getCharacterByName(page: String, name: String): Call<CharactersResponse> {
        val apiService = ApiConfig.getApiService()
        return apiService.getCharacterByName(page, name)
    }

    suspend fun getCharacterById(id: String): Call<ResultsItem> {
        val apiService = ApiConfig.getApiService()
        return apiService.getCharacterById(id)
    }

    fun getAllFavourite(): LiveData<List<Favourite>> = favouriteDao.getAllFav()

    fun getFavourited(id: String): LiveData<List<Favourite>> = favouriteDao.getFavorited(id)

    fun insertFavourite(favourite: Favourite) {
        executorService.execute { favouriteDao.insert(favourite) }
    }

    fun deleteFavourite(favourite: Favourite) {
        executorService.execute { favouriteDao.delete(favourite) }
    }

    companion object {
        @Volatile
        private var instance: CharacterRepository? = null

        fun getInstance(context: Context) : CharacterRepository =
            instance ?: synchronized(this) {
                instance ?: CharacterRepository(context)
                    .also {
                        instance = it
                    }
            }
    }
}