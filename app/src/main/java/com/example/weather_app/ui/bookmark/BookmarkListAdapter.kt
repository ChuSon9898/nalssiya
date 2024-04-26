package com.example.weather_app.ui.bookmark

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.di.RetrofitModule
import com.example.weather_app.data.retrofit.RetrofitApi
import com.example.weather_app.databinding.BookmarkRvItemBinding
import com.example.weather_app.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BookmarkListAdapter :
    ListAdapter<BookmarkDataModel, BookmarkListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<BookmarkDataModel>() {
            override fun areItemsTheSame(
                oldBookmarkItem: BookmarkDataModel,
                newBookmarkItem: BookmarkDataModel
            ): Boolean {
                return oldBookmarkItem.id == newBookmarkItem.id
            }

            override fun areContentsTheSame(
                oldBookmarkItem: BookmarkDataModel,
                newBookmarkItem: BookmarkDataModel
            ): Boolean {
                return oldBookmarkItem == newBookmarkItem
            }
        }) {

    interface OnItemClickListener {
        fun onItemClick(item: BookmarkDataModel, position: Int)
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

        val swipeLayout: CardView = binding.bookmarkCv

        @SuppressLint("SetTextI18n")
        fun bind(bookmarkItem: BookmarkDataModel) {
            with(binding) {
                bookmarkTvTemp.visibility = View.INVISIBLE
                bookmarkTvMaxTemp.visibility = View.INVISIBLE
                bookmarkTvMinTemp.visibility = View.INVISIBLE

                if (bookmarkItem.Dong.isEmpty()) {
                    bookmarkTvLocation.text = bookmarkItem.Gu
                } else {
                    bookmarkTvLocation.text = bookmarkItem.Dong
                }

                bookmarkTvTemp.text = "${bookmarkItem.temp}°"
                bookmarkTvMinTemp.text = "${bookmarkItem.minTemp}°"
                bookmarkTvMaxTemp.text = "${bookmarkItem.maxTemp}°"

                bookmarkTvTemp.visibility = View.VISIBLE
                bookmarkTvMaxTemp.visibility = View.VISIBLE
                bookmarkTvMinTemp.visibility = View.VISIBLE

                itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(bookmarkItem, adapterPosition)
                }

            }
        }
    }
}