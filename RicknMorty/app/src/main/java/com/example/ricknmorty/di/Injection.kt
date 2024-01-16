package com.example.ricknmorty.di

import android.content.Context
import com.example.ricknmorty.data.repository.CharacterRepository

object Injection {
    fun provideRepository(context: Context): CharacterRepository {
        return CharacterRepository.getInstance()
    }
}