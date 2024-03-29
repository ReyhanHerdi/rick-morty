package com.example.herdiyanto.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.herdiyanto.R
import com.example.herdiyanto.data.response.ResultsItem
import com.example.herdiyanto.data.room.Favourite
import com.example.herdiyanto.databinding.ActivityDetailBinding
import com.example.herdiyanto.ui.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel.viewModelScope.launch {
            getData()
        }
    }

    fun getData() {
        try {
            isLoading(true)
            detailViewModel.getCharacterById("${intent.getStringExtra(CHARACTER_ID)}")
                .enqueue(object : Callback<ResultsItem> {
                override fun onResponse(
                    call: Call<ResultsItem>,
                    response: Response<ResultsItem>,
                ) {
                    if (response.isSuccessful) {
                        isLoading(false)
                        val responseBody = response.body()
                        with(binding) {
                            tvProfileName.text = responseBody?.name
                            tvProfileStatus.text = responseBody?.status
                            tvProfileSpecies.text = responseBody?.species
                            tvProfileGender.text = responseBody?.gender
                            tvProfileOrigin.text = responseBody?.origin?.name
                            tvProfileLocation.text = responseBody?.location?.name
                            Glide.with(this@DetailActivity)
                                .load(responseBody?.image)
                                .into(ivProfileImage)
                        }

                        favorited(
                            Favourite(
                                responseBody?.id.toString(),
                                responseBody?.name ,
                                responseBody?.status,
                                responseBody?.species,
                                responseBody?.gender,
                                responseBody?.origin?.name,
                                responseBody?.location?.name,
                                responseBody?.image
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResultsItem>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }

            })
        } catch (e: Exception) {
            Log.d("ERROR", e.message.toString())
        }
    }

    private fun favorited(favourite: Favourite) {
        detailViewModel.getFavourited(favourite.id).observe(this) { findFavourite ->
            if (findFavourite.isNullOrEmpty()) {
                binding.fabFavourite.setImageResource(R.drawable.baseline_favorite_border_24)
                insertFavourite(favourite)
            } else {
                binding.fabFavourite.setImageResource(R.drawable.baseline_favorite_24)
                deleteFavourite(favourite)
            }
        }
    }

    private fun insertFavourite(favourite: Favourite) {
        binding.fabFavourite.setOnClickListener {
            detailViewModel.insertFavourite(
                Favourite(
                favourite.id,
                favourite.name,
                favourite.status,
                favourite.species,
                favourite.gender,
                favourite.origin,
                favourite.location,
                favourite.image
            ))
            binding.fabFavourite.setImageResource(R.drawable.baseline_favorite_24)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteFavourite(favourite: Favourite) {
        binding.fabFavourite.setOnClickListener {
            detailViewModel.deleteFavourite(
                Favourite(
                    favourite.id,
                    favourite.name,
                    favourite.status,
                    favourite.species,
                    favourite.gender,
                    favourite.origin,
                    favourite.location,
                    favourite.image
                )
            )
            binding.fabFavourite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val CHARACTER_ID = "character_id"
    }
}