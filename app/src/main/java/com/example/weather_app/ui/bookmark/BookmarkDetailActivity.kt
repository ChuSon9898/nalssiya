package com.example.weather_app.ui.bookmark

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.repository.room.BookmarkRepositoryImpl
import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.databinding.HomeActivityBinding
import com.example.weather_app.ui.home.DailyListAdapter
import com.example.weather_app.ui.home.HomeViewModel
import com.example.weather_app.ui.home.HourlyListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkDetailActivity : AppCompatActivity() {
    private lateinit var binding: HomeActivityBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private val bookmarkViewModel : BookmarkViewModel by viewModels()

    private val hourlyAdapter by lazy {
        HourlyListAdapter()
    }
    private val dailyAdapter by lazy {
        DailyListAdapter()
    }


    companion object {
        const val OBJECT_DATA = "item_object"
        fun detailIntent(context: Context, item : BookmarkDataModel): Intent {
            val intent = Intent(context, BookmarkDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }

    private val item : BookmarkDataModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, BookmarkDataModel::class.java)
        } else {
            intent.getParcelableExtra<BookmarkDataModel>(OBJECT_DATA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        pbHourly.visibility = View.VISIBLE
        pbDaily.visibility = View.VISIBLE

        rvHourly.adapter = hourlyAdapter
        rvDaily.adapter = dailyAdapter

        rvHourly.layoutManager = LinearLayoutManager(this@BookmarkDetailActivity, LinearLayoutManager.HORIZONTAL, true)
        rvDaily.layoutManager = LinearLayoutManager(this@BookmarkDetailActivity, LinearLayoutManager.VERTICAL, false)

        tvCancel.setOnClickListener {
            finish()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val addBookmark = bookmarkViewModel.getDataByLocation(item!!)

            if(addBookmark.isNotEmpty()){
                tvAdd.visibility = View.INVISIBLE
            }
        }

        tvAdd.setOnClickListener {
            val newBookmark = BookmarkDataModel(0,item!!.Gu, item!!.Dong, item!!.nx, item!!.ny, item!!.landArea,  item!!.tempArea)
            intent.putExtra("newBookmark", newBookmark)
            setResult(RESULT_OK, intent)
            finish()
        }

        ivList.visibility = View.GONE

        if(item!!.Dong.isEmpty()){
            tvLocation.text = item!!.Gu
        }else{
            tvLocation.text = item!!.Dong
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() = with(homeViewModel) {
        getHourlyWeather(item!!.nx, item!!.ny)
        getDailyWeather(item!!.nx, item!!.ny, item!!.tempArea, item!!.landArea)

        hourlyList.observe(this@BookmarkDetailActivity, Observer { hourlyList ->
            hourlyAdapter.submitList(hourlyList)
            binding.pbHourly.visibility = View.GONE
        })
        dailyList.observe(this@BookmarkDetailActivity, Observer { dailyList ->
            dailyAdapter.submitList(dailyList)
            binding.pbDaily.visibility = View.GONE
        })
        currentWeather.observe(this@BookmarkDetailActivity, Observer { weather ->
            with(binding) {
                tvTemp.text = "${weather!!.temp}°"

                when (weather.rainType.toInt()) {
                    0 -> {
                        when (weather.sky.toInt()) {
                            1 -> tvWeather.text = "맑음"
                            3 -> tvWeather.text = "대체로 흐림"
                            4 -> tvWeather.text = "흐림"
                        }
                    }
                    in 1..2, 4 -> tvWeather.text = "비"
                    3 -> tvWeather.text = "눈"
                }

                tvWind.text = "${weather.windSpd}m/s"

                when (weather.windDir.toInt()) {
                    in 0..44 -> tvWindDirection.text = "북 - 북동"
                    in 45..89 -> tvWindDirection.text = "북동 - 동"
                    in 90..134 -> tvWindDirection.text = "동 - 남동"
                    in 135..179 -> tvWindDirection.text = "남동 - 남"
                    in 180..224 -> tvWindDirection.text = "남 - 남서"
                    in 225..269 -> tvWindDirection.text = "남서 - 서"
                    in 270..314 -> tvWindDirection.text = "서 - 북서"
                    in 315..359 -> tvWindDirection.text = "북서 - 북"
                }

                tvHumidity.text = "${weather.humidity}%"

                when (weather.rainHour) {
                    "강수없음" -> tvRain.text = "0mm"
                    else -> tvRain.text = weather.rainHour
                }

                when (weather.snowHour) {
                    "적설없음" -> tvSnow.text = "0cm"
                    else -> tvSnow.text = weather.snowHour
                }
            }
        })

        currentWeather2.observe(this@BookmarkDetailActivity, Observer {weather ->
            with(binding) {
                tvMaxtemp.text = "최고:${weather.maxTemp}°"
                tvMintemp.text = "최저:${weather.minTemp}°"
            }
        })
    }
}