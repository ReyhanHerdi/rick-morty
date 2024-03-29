package com.example.herdiyanto.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.herdiyanto.data.response.ResultsItem
import com.example.herdiyanto.databinding.ItemCharactersBinding

class CharacterAdapter : ListAdapter<ResultsItem, CharacterAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private val binding: ItemCharactersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataList: ResultsItem) {
            with(binding) {
                tvCharacterName.text = dataList.name
                tvCharacterSpecies.text = dataList.species
                tvCharacterStatus.text = dataList.status
                Glide.with(itemView.context)
                    .load(dataList.image)
                    .into(ivCharacterImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCharactersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataList = getItem(position)
        holder.bind(dataList)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataList)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }

        }
    }

}