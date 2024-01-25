package com.example.weather_app.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.data.model.SearchLocation
import com.example.weather_app.databinding.BookmarkRvItemBinding
import com.example.weather_app.databinding.BookmarkSearchRvItemBinding
import okhttp3.internal.notify

class BookmarkSearchListAdapter :
    ListAdapter<SearchLocation, BookmarkSearchListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<SearchLocation>() {
            override fun areItemsTheSame(
                oldBookmarkSearchItem: SearchLocation,
                newBookmarkSearchItem: SearchLocation
            ): Boolean {
                return oldBookmarkSearchItem.id == newBookmarkSearchItem.id
            }

            override fun areContentsTheSame(
                oldBookmarkSearchItem: SearchLocation,
                newBookmarkSearchItem: SearchLocation
            ): Boolean {
                return oldBookmarkSearchItem == newBookmarkSearchItem
            }
        }) {

    interface OnItemClickListener {
        fun onItemClick(item: SearchLocation, position:Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    public override fun getItem(position: Int): SearchLocation {
        return super.getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookmarkSearchRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: BookmarkSearchRvItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchItem: SearchLocation) {
            with(binding) {

                bookmarkSearchTvList.text = searchItem.Gu +  " " + searchItem.Dong

                itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(searchItem, adapterPosition)
                }

            }
        }
    }
}