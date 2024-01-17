package com.example.herdiyanto.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.herdiyanto.data.repository.CharacterRepository
import com.example.herdiyanto.data.response.CharactersResponse
import retrofit2.Call

class SearchViewModel(private val repository: CharacterRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun getCharacterByName(page: String, name: String): Call<CharactersResponse> {
        return repository.getCharacterByName(page, name)
    }
}