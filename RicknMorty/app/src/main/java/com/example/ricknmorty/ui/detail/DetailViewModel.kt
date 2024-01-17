package com.example.ricknmorty.ui.detail

import androidx.lifecycle.ViewModel
import com.example.ricknmorty.data.repository.CharacterRepository
import com.example.ricknmorty.data.response.CharactersResponse
import com.example.ricknmorty.data.response.ResultsItem
import retrofit2.Call

class DetailViewModel(private val repository: CharacterRepository) : ViewModel() {
    suspend fun getCharacterById(id: String) : Call<ResultsItem> {
        return repository.getCharacterById(id)
    }

}