package com.example.herdiyanto.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.herdiyanto.data.room.Favourite
import com.example.herdiyanto.databinding.ItemCharactersBinding

class FavouritedAdapter : ListAdapter<Favourite, FavouritedAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Favourite)
        fun onBtnFavClicked(data: Favourite, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private val binding: ItemCharactersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataList: Favourite) {
            with(binding) {
                tvCharacterName.text = dataList.name
                tvCharacterSpecies.text = dataList.species
                tvCharacterStatus.text = dataList.status
                Glide.with(binding.root)
                    .load(dataList.image)
                    .into(ivCharacterImage)

                btnFavourite.visibility = View.VISIBLE
            }
        }
        val btnFavourite = binding.btnFavourite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCharactersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataList = getItem(position)
        holder.bind(dataList)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataList)
        }

        holder.btnFavourite.setOnClickListener {
            onItemClickCallback.onBtnFavClicked(dataList, position)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favourite>() {
            override fun areItemsTheSame(oldItem: Favourite, newItem: Favourite): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Favourite, newItem: Favourite): Boolean {
                return oldItem == newItem
            }

        }
    }
}