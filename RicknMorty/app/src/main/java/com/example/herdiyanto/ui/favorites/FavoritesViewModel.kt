package com.example.herdiyanto.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.herdiyanto.data.repository.CharacterRepository
import com.example.herdiyanto.data.room.Favourite

class FavoritesViewModel(private val repository: CharacterRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    fun getAllFavourited(): LiveData<List<Favourite>> =
        repository.getAllFavourite()

    fun deleteFavourite(favourite: Favourite) {
        repository.deleteFavourite(favourite)
    }
}