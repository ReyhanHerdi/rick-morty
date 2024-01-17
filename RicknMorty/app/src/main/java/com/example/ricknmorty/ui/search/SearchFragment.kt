package com.example.ricknmorty.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ricknmorty.data.response.CharactersResponse
import com.example.ricknmorty.data.response.ResultsItem
import com.example.ricknmorty.databinding.FragmentSearchBinding
import com.example.ricknmorty.ui.ViewModelFactory
import com.example.ricknmorty.ui.adapter.CharacterAdapter
import com.example.ricknmorty.ui.detail.DetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchCharacter()

        return root
    }

    private suspend fun getData(page: String, name: String) {
        try {
            searchViewModel.getCharacterByName(page, name)
                .enqueue(object : Callback<CharactersResponse> {
                    override fun onResponse(
                        call: Call<CharactersResponse>,
                        response: Response<CharactersResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            setAdapter(responseBody?.results)
                            binding.tvEmpty.visibility = View.GONE
                        } else {
                            binding.tvEmpty.visibility = View.VISIBLE
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
        binding.rvCharactersSearch.layoutManager = layoutManager
        binding.rvCharactersSearch.adapter = adapter

        adapter.setOnItemClickCallback(object : CharacterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultsItem) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.CHARACTER_ID, "${data.id}")
                startActivity(intent)
            }
        })

    }

    private fun searchCharacter() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        getData(PAGE, "${searchView.text}")
                    }
                    false
                }
        }
    }

    companion object {
        const val PAGE = "1"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}