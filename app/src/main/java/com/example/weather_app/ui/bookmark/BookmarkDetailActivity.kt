package com.example.weather_app.ui.bookmark

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.model.SearchLocation
import com.example.weather_app.databinding.HomeActivityBinding
import com.example.weather_app.ui.home.DailyListAdapter
import com.example.weather_app.ui.home.HomeActivity
import com.example.weather_app.ui.home.HomeViewModel
import com.example.weather_app.ui.home.HomeViewModelFactory
import com.example.weather_app.ui.home.HourlyListAdapter

class BookmarkDetailActivity : AppCompatActivity() {
    private lateinit var binding: HomeActivityBinding

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    private val bookmarkViewModel by lazy {
        ViewModelProvider(this).get(BookmarkViewModel::class.java)
    }

    private val hourlyAdapter by lazy {
        HourlyListAdapter()
    }
    private val dailyAdapter by lazy {
        DailyListAdapter()
    }

    companion object {
        const val OBJECT_DATA = "item_object"
        fun detailIntent(context: Context, item : SearchLocation): Intent {
            val intent = Intent(context, BookmarkDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }

    private val item : SearchLocation? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, SearchLocation::class.java)
        } else {
            intent.getParcelableExtra<SearchLocation>(OBJECT_DATA)
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
        rvHourly.adapter = hourlyAdapter
        rvDaily.adapter = dailyAdapter

        rvHourly.layoutManager = LinearLayoutManager(this@BookmarkDetailActivity, LinearLayoutManager.HORIZONTAL, true)
        rvDaily.layoutManager = LinearLayoutManager(this@BookmarkDetailActivity, LinearLayoutManager.VERTICAL, false)

        tvCancel.setOnClickListener {
            finish()
        }

        tvAdd.setOnClickListener {
            bookmarkViewModel.insertData(item!!.Dong, item!!.nx, item!!.ny, item!!.landArea, item!!.tempArea)
            finish()
        }
        ivList.visibility = View.GONE
        tvLocation.text = item!!.Dong
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() = with(homeViewModel) {
        getHourlyWeather(item!!.nx, item!!.ny)
        getDailyWeather(item!!.nx, item!!.ny)

        hourlyList.observe(this@BookmarkDetailActivity, Observer { hourlyList ->
            hourlyAdapter.submitList(hourlyList)
        })
        dailyList.observe(this@BookmarkDetailActivity, Observer { threeDayList ->
            dailyAdapter.submitList(threeDayList)
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