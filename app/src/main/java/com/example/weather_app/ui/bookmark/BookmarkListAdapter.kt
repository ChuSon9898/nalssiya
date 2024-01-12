package com.example.weather_app.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.databinding.BookmarkRvItemBinding

class BookmarkListAdapter :
    ListAdapter<BookmarkDataModel, BookmarkListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<BookmarkDataModel>() {
            override fun areItemsTheSame(
                oldFavoriteItem: BookmarkDataModel,
                newFavoriteItem: BookmarkDataModel
            ): Boolean {
                return oldFavoriteItem.id == newFavoriteItem.id
            }

            override fun areContentsTheSame(
                oldFavoriteItem: BookmarkDataModel,
                newFavoriteItem: BookmarkDataModel
            ): Boolean {
                return oldFavoriteItem == newFavoriteItem
            }
        }) {

    interface OnItemClickListener {
        fun onItemClick(item: BookmarkDataModel, position:Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookmarkRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: BookmarkRvItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteItem: BookmarkDataModel) {
            with(binding) {

                if (favoriteItem.id == 0) {
                    cardViewLayout.setBackgroundColor(ContextCompat.getColor(root.context,
                        R.color.light_blue
                    ))
                } else {
                    cardViewLayout.setBackgroundColor(ContextCompat.getColor(root.context,
                        R.color.dark_blue
                    ))
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