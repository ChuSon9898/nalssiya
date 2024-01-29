package com.example.weather_app.ui.bookmark

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.R
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.retrofit.RetrofitClient
import com.example.weather_app.data.retrofit.RetrofitInterface
import com.example.weather_app.databinding.BookmarkRvItemBinding
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
                val currentTime = LocalTime.now()

                if (!currentTime.isBefore(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(18, 0))) {
                    cardViewLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.light_blue
                        )
                    )
                }
                else {
                    cardViewLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.dark_blue
                        )
                    )
                }

                if (bookmarkItem.Dong.isEmpty()) {
                    bookmarkTvLocation.text = bookmarkItem.Gu
                } else {
                    bookmarkTvLocation.text = bookmarkItem.Dong
                }

                //API 데이터
                val client = RetrofitClient.getInstance().create(RetrofitInterface::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("itemInfo", bookmarkItem.toString())
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
                        getBaseDate(LocalDateTime.now()),
                        getBaseTime(LocalTime.now()),
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

                    Log.d("itemInfo", "${minTemp}, ${maxTemp}, ${temp}")

                    withContext(Dispatchers.Main) {
                        bookmarkTvMaxAndMin.text = "최고 ${maxTemp}° 최저 ${minTemp}°"
                        bookmarkTvTemp.text = "${temp}°"
                    }
                }

                itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(bookmarkItem, adapterPosition)
                }

            }
        }

        //BaseTime 계산 함수
        private fun getBaseTime(time: LocalTime): String {
            var baseTime = ""
            if (!time.isBefore(LocalTime.of(3, 0)) && time.isBefore(LocalTime.of(6, 0))) baseTime =
                "0200"
            else if (!time.isBefore(LocalTime.of(6, 0)) && time.isBefore(
                    LocalTime.of(
                        9,
                        0
                    )
                )
            ) baseTime =
                "0500"
            else if (!time.isBefore(LocalTime.of(9, 0)) && time.isBefore(
                    LocalTime.of(
                        12,
                        0
                    )
                )
            ) baseTime = "0800"
            else if (!time.isBefore(LocalTime.of(12, 0)) && time.isBefore(
                    LocalTime.of(
                        15,
                        0
                    )
                )
            ) baseTime = "1100"
            else if (!time.isBefore(LocalTime.of(15, 0)) && time.isBefore(
                    LocalTime.of(
                        18,
                        0
                    )
                )
            ) baseTime = "1400"
            else if (!time.isBefore(LocalTime.of(18, 0)) && time.isBefore(
                    LocalTime.of(
                        21,
                        0
                    )
                )
            ) baseTime = "1700"
            else if (!time.isBefore(LocalTime.of(21, 0)) && time.isBefore(
                    LocalTime.of(
                        23,
                        59,
                        59,
                    )
                )
            ) baseTime = "2000"
            else if (!time.isBefore(LocalTime.of(0, 0)) && time.isBefore(
                    LocalTime.of(
                        3,
                        0
                    )
                )
            ) baseTime = "2300"

            return baseTime
        }

        //BaseDate 계산 함수
        private fun getBaseDate(time: LocalDateTime): Int {
            return if (time.hour in 0 until 3) time.minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
            else time.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
        }
    }
}