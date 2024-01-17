package com.example.ricknmorty.ui.favorites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ricknmorty.data.room.Favourite
import com.example.ricknmorty.databinding.FragmentFavoritesBinding
import com.example.ricknmorty.ui.ViewModelFactory
import com.example.ricknmorty.ui.adapter.FavouritedAdapter
import com.example.ricknmorty.ui.detail.DetailActivity

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favouriteViewModel by viewModels<FavoritesViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: FavouritedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getData()

        return root
    }

    private fun getData() {
        favouriteViewModel.getAllFavourited().observe(viewLifecycleOwner) { favourite ->
            if (!favourite.isNullOrEmpty()) {
                setAdapter(favourite)
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun setAdapter(dataList: List<Favourite>) {
        adapter = FavouritedAdapter()
        adapter.submitList(dataList)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFavourite.adapter = adapter
        binding.rvFavourite.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : FavouritedAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favourite) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.CHARACTER_ID, data.id)

                startActivity(intent)
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onBtnFavClicked(data: Favourite, position: Int) {
                favouriteViewModel.deleteFavourite(data)
                adapter.notifyItemRemoved(position)

                val updatedList = adapter.currentList.toMutableList()
                updatedList.removeAt(position)
                adapter.submitList(updatedList)

                adapter.notifyDataSetChanged()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}