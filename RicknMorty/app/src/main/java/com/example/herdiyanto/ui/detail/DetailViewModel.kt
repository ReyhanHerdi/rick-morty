package com.example.herdiyanto.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.herdiyanto.data.repository.CharacterRepository
import com.example.herdiyanto.data.response.ResultsItem
import com.example.herdiyanto.data.room.Favourite
import retrofit2.Call

class DetailViewModel(private val repository: CharacterRepository) : ViewModel() {
    fun getCharacterById(id: String) : Call<ResultsItem> {
        return repository.getCharacterById(id)
    }

    fun getFavourited(id: String): LiveData<List<Favourite>> =
        repository.getFavourited(id)

    fun insertFavourite(favourite: Favourite) {
        repository.insertFavourite(favourite)
    }

    fun deleteFavourite(favourite: Favourite) {
        repository.deleteFavourite(favourite)
    }

}