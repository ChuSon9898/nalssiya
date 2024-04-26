package com.example.weather_app.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.databinding.BookmarkSearchRvItemBinding
class BookmarkSearchListAdapter :
    ListAdapter<BookmarkDataModel, BookmarkSearchListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<BookmarkDataModel>() {
            override fun areItemsTheSame(
                oldBookmarkSearchItem: BookmarkDataModel,
                newBookmarkSearchItem: BookmarkDataModel
            ): Boolean {
                return oldBookmarkSearchItem.id == newBookmarkSearchItem.id
            }

            override fun areContentsTheSame(
                oldBookmarkSearchItem: BookmarkDataModel,
                newBookmarkSearchItem: BookmarkDataModel
            ): Boolean {
                return oldBookmarkSearchItem == newBookmarkSearchItem
            }
        }) {

    interface OnItemClickListener {
        fun onItemClick(item: BookmarkDataModel, position:Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    public override fun getItem(position: Int): BookmarkDataModel {
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
        fun bind(searchItem: BookmarkDataModel) {
            with(binding) {

                bookmarkSearchTvList.text = searchItem.Gu +  " " + searchItem.Dong

                itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(searchItem, adapterPosition)
                }

            }
        }
    }
}