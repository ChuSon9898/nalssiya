package com.example.weather_app

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.databinding.FavoriteRvItemBinding

class FavoriteListAdapter :
    ListAdapter<FavoriteModel, FavoriteListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<FavoriteModel>() {
            override fun areItemsTheSame(
                oldFavoriteItem: FavoriteModel,
                newFavoriteItem: FavoriteModel
            ): Boolean {
                return oldFavoriteItem.id == newFavoriteItem.id
            }

            override fun areContentsTheSame(
                oldFavoriteItem: FavoriteModel,
                newFavoriteItem: FavoriteModel
            ): Boolean {
                return oldFavoriteItem == newFavoriteItem
            }
        }) {

    interface OnItemClickListener {
        fun onItemClick(item: FavoriteModel, position:Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FavoriteRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: FavoriteRvItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteItem: FavoriteModel) {
            with(binding) {

                if (favoriteItem.id == 0) {
                    cardViewLayout.setBackgroundColor(ContextCompat.getColor(root.context, R.color.light_blue))
                } else {
                    cardViewLayout.setBackgroundColor(ContextCompat.getColor(root.context, R.color.dark_blue))
                }

                favoriteTvLocation.text = favoriteItem.location
                favoriteTvTime.text = favoriteItem.time
                favoriteTvTemp.text = favoriteItem.temp
                favoriteTvMaxAndMin.text = favoriteItem.maxMin

                itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(favoriteItem, adapterPosition)
                }
            }
        }
    }
}