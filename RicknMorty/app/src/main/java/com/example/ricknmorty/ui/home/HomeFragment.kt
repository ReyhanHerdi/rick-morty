package com.example.ricknmorty.ui.home

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ricknmorty.data.response.CharactersResponse
import com.example.ricknmorty.data.response.ResultsItem
import com.example.ricknmorty.databinding.FragmentHomeBinding
import com.example.ricknmorty.ui.ViewModelFactory
import com.example.ricknmorty.ui.adapter.CharacterAdapter
import com.example.ricknmorty.ui.detail.DetailActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CharacterAdapter
    private val application = Application()
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

    suspend fun getData(page: String) {
        try {
            val getCharacter = homeViewModel.getCharacter(page)
            getCharacter.enqueue(object : Callback<CharactersResponse> {
                override fun onResponse(
                    call: Call<CharactersResponse>,
                    response: Response<CharactersResponse>,
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        setAdapter(responseBody?.results)
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

//    private fun showClickedCharacter(data: ResultsItem) {
//        Toast.makeText(context, "${data.id}", Toast.LENGTH_SHORT).show()
////        val intent = Intent(context, DetailActivity::class.java)
////        intent.putExtra(DetailActivity.CHARACTER_ID, data.id)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}