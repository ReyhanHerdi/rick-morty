package com.example.ricknmorty.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.ricknmorty.data.response.ResultsItem
import com.example.ricknmorty.databinding.ActivityDetailBinding
import com.example.ricknmorty.ui.ViewModelFactory
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

    suspend fun getData() {
        try {
            detailViewModel.getCharacterById("${intent.getStringExtra(CHARACTER_ID)}")
                .enqueue(object : Callback<ResultsItem> {
                override fun onResponse(
                    call: Call<ResultsItem>,
                    response: Response<ResultsItem>,
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        with(binding) {
                            tvProfileName.text = responseBody?.name
                            tvProfileStatus.text = responseBody?.status
                            tvProfileGender.text = responseBody?.gender
                            tvProfileOrigin.text = responseBody?.origin?.name
                            tvProfileLocation.text = responseBody?.location?.name
                            Glide.with(this@DetailActivity)
                                .load(responseBody?.image)
                                .into(ivProfileImage)
                        }
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

    companion object {
        const val CHARACTER_ID = "character_id"
    }
}