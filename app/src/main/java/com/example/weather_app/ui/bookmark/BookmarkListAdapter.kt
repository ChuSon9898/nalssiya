package com.example.weather_app.ui.bookmark

import android.nfc.cardemulation.CardEmulation
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.model.SearchLocation
import com.example.weather_app.databinding.BookmarkRvItemBinding
import okhttp3.internal.notify

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

        val swipeLayout : CardView = binding.bookmarkCv

        fun bind(bookmarkItem: BookmarkDataModel) {
            with(binding) {

                if (bookmarkItem.id == 1) {
                    cardViewLayout.setBackgroundColor(ContextCompat.getColor(root.context,
                        R.color.light_blue
                    ))

                    //API로 데이터 가져와서 업데이트
                    bookmarkTvTime.text = "서울특별시"

                } else {
                    cardViewLayout.setBackgroundColor(ContextCompat.getColor(root.context,
                        R.color.dark_blue
                    ))

                    //시간 가져오기!
                    bookmarkTvTime.text = "오후 16:00"
                }

                bookmarkTvLocation.text = bookmarkItem.location

                //API로 데이터 가져와서 업데이트
                bookmarkTvTemp.text = "8°"
                bookmarkTvMaxAndMin.text = "최고 -2° 최저 -8°"

            }
        }
    }
}