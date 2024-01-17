package com.example.herdiyanto.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.herdiyanto.data.response.CharactersResponse
import com.example.herdiyanto.data.response.ResultsItem
import com.example.herdiyanto.databinding.FragmentSearchBinding
import com.example.herdiyanto.ui.ViewModelFactory
import com.example.herdiyanto.ui.adapter.CharacterAdapter
import com.example.herdiyanto.ui.detail.DetailActivity
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

    private fun getData(page: String, name: String) {
        try {
            isLoading(true)
            searchViewModel.getCharacterByName(page, name)
                .enqueue(object : Callback<CharactersResponse> {
                    override fun onResponse(
                        call: Call<CharactersResponse>,
                        response: Response<CharactersResponse>,
                    ) {
                        val responseBody = response.body()
                        if (response.isSuccessful) {
                            setAdapter(responseBody?.results)
                            binding.tvEmpty.visibility = View.GONE
                            isLoading(false)
                        } else {
                            setAdapter(responseBody?.results)
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

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
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