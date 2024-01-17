package com.example.herdiyanto.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.herdiyanto.data.response.CharactersResponse
import com.example.herdiyanto.data.response.ResultsItem
import com.example.herdiyanto.databinding.FragmentHomeBinding
import com.example.herdiyanto.ui.ViewModelFactory
import com.example.herdiyanto.ui.adapter.CharacterAdapter
import com.example.herdiyanto.ui.detail.DetailActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CharacterAdapter

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.viewModelScope.launch {
            getData("1")
        }
        return root
    }

    fun getData(page: String) {
        try {
            isLoading(true)
            val getCharacter = homeViewModel.getCharacter(page)
            getCharacter.enqueue(object : Callback<CharactersResponse> {
                override fun onResponse(
                    call: Call<CharactersResponse>,
                    response: Response<CharactersResponse>,
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        setAdapter(responseBody?.results)
                        isLoading(false)
                    } else {
                        binding.tvEmpty.visibility = View.VISIBLE
                        isLoading(false)
                    }
                }

                override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }

            })
        } catch (e: Exception) {
            Log.d("ERROR", e.message.toString())
        }
    }

    private fun setAdapter(dataList: List<ResultsItem?>?) {
        adapter = CharacterAdapter()
        adapter.submitList(dataList)

        val layoutManager = LinearLayoutManager(context)
        binding.rvCharacters.layoutManager = layoutManager
        binding.rvCharacters.adapter = adapter

        adapter.setOnItemClickCallback(object : CharacterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultsItem) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.CHARACTER_ID, "${data.id}")
                startActivity(intent)
            }
        })
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}