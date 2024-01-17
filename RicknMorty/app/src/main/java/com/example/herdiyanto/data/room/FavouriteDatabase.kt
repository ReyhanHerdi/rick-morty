package com.example.herdiyanto.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favourite::class], version = 1)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavouriteDatabase {
            if (INSTANCE == null) {
                synchronized(FavouriteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavouriteDatabase::class.java, "favourite"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as FavouriteDatabase
        }
    }
}