package com.example.herdiyanto.di

import android.content.Context
import com.example.herdiyanto.data.repository.CharacterRepository

object Injection {
    fun provideRepository(context: Context): CharacterRepository {
        return CharacterRepository.getInstance(context)
    }
}