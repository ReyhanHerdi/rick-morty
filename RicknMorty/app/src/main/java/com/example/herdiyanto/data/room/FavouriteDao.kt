package com.example.herdiyanto.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM favourite")
    fun getAllFav(): LiveData<List<Favourite>>

    @Query("SELECT * FROM favourite WHERE id = :id")
    fun getFavorited(id: String): LiveData<List<Favourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favourite: Favourite)

    @Delete
    fun delete(favourite: Favourite)
}