package com.example.herdiyanto.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.herdiyanto.data.repository.CharacterRepository
import com.example.herdiyanto.data.response.CharactersResponse
import retrofit2.Call

class HomeViewModel(private val repository: CharacterRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    fun getCharacter(page: String): Call<CharactersResponse> {
        return repository.getCharacter(page)
    }


}