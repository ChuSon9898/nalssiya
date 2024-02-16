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
import com.example.weather_app.data.retrofit.RetrofitClient
import com.example.weather_app.data.retrofit.RetrofitInterface
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

                //API 데이터
                val client = RetrofitClient.getInstance().create(RetrofitInterface::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    val responseMinMax = client.getHourlyWeatehr(
                        "JSON",
                        200,
                        1,
                        LocalDateTime.now().minusDays(1)
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt(),
                        "2300",
                        bookmarkItem.nx,
                        bookmarkItem.ny
                    )
                    val minMaxList = responseMinMax.body()?.response!!.body.items.item

                    //최저, 최고 온도
                    val minTemp = minMaxList.firstOrNull { it.category == "TMN" }?.fcstValue ?: ""
                    val maxTemp = minMaxList.firstOrNull { it.category == "TMX" }?.fcstValue ?: ""

                    val responseTemp = client.getHourlyWeatehr(
                        "JSON",
                        100,
                        1,
                        Utils.getBaseDate(LocalDateTime.now()),
                        Utils.getBaseTime(LocalTime.now()),
                        bookmarkItem.nx,
                        bookmarkItem.ny
                    )
                    val tempList = responseTemp.body()?.response!!.body.items.item

                    //현재 온도
                    val temp = tempList.firstOrNull {
                        it.category == "TMP" && it.fcstTime == "${
                            LocalTime.now().format(DateTimeFormatter.ofPattern("HH"))
                        }00"
                    }?.fcstValue ?: ""

                    withContext(Dispatchers.Main) {
                        bookmarkTvMinTemp.text = "${minTemp}°"
                        bookmarkTvMaxTemp.text = "${maxTemp}°"
                        bookmarkTvTemp.text = "${temp}°"

                        bookmarkTvTemp.visibility = View.VISIBLE
                        bookmarkTvMaxTemp.visibility = View.VISIBLE
                        bookmarkTvMinTemp.visibility = View.VISIBLE
                    }
                }

                itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(bookmarkItem, adapterPosition)
                }

            }
        }
    }
}