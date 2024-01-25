package com.example.weather_app.ui.home

import android.annotation.SuppressLint
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.HomeActivityBinding
import com.example.weather_app.util.RequestPermissionsUtil
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.example.weather_app.ui.bookmark.BookmarkActivity.Companion.bookmarkIndent
import com.example.weather_app.ui.bookmark.BookmarkDetailActivity
import com.example.weather_app.ui.bookmark.BookmarkDetailActivity.Companion.detailIntent
import java.io.IOException
import java.util.Locale

open class HomeActivity : AppCompatActivity() {
    //앱 실행 시, 위치 권한 묻기
    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation()
    }

    private lateinit var binding: HomeActivityBinding

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    private val hourlyAdapter by lazy {
        HourlyListAdapter()
    }
    private val dailyAdapter by lazy {
        DailyListAdapter()
    }

    //기본 위치 좌표 (서울시청)
    private var latitude = 37.564214
    private var longitude = 127.001699

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLocation()
        initView()
    }

    private fun initView() = with(binding) {
        rvHourly.adapter = hourlyAdapter
        rvDaily.adapter = dailyAdapter

        rvHourly.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, true)
        rvDaily.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)

        ivList.setOnClickListener {
            startActivity(bookmarkIndent(this@HomeActivity))
        }
        button.setOnClickListener {
            startActivity(detailIntent(this@HomeActivity))
        }

        tvCancel.visibility = View.GONE
        tvAdd.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel(point: Point) = with(viewModel) {
        getHourlyWeather(point.x.toString(), point.y.toString())
        getDailyWeather(point.x.toString(), point.y.toString())

        hourlyList.observe(this@HomeActivity, Observer { hourlyList ->
            hourlyAdapter.submitList(hourlyList)
        })
        dailyList.observe(this@HomeActivity, Observer { threeDayList ->
            dailyAdapter.submitList(threeDayList)
        })
        currentWeather.observe(this@HomeActivity, Observer { weather ->
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

        currentWeather2.observe(this@HomeActivity, Observer {weather ->
            with(binding) {
                tvMaxtemp.text = "최고:${weather.maxTemp}°"
                tvMintemp.text = "최저:${weather.minTemp}°"
            }
        })
    }

    //위치 정보 받아오는 함수
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { success : Location? ->
                success?.let { location ->
                    latitude = location.latitude
                    longitude = location.longitude
                    initViewModel(dfsXyConv(latitude, longitude))
                    Log.d("HomeActivity", "성공")
                    val address = getAddress(latitude, longitude)?.get(0)
                    Log.d("HomeActivity", address.toString())
                }
            }
            .addOnFailureListener { fail ->
                initViewModel(dfsXyConv(latitude, longitude))
                Log.d("Location", "실패")
                Toast.makeText(this, "위치를 조회할 수 없습니다", Toast.LENGTH_LONG).show()
            }
    }

    // 위, 경도 -> 격자 좌표 변환 함수
    private fun dfsXyConv(v1: Double, v2: Double) : Point {
        val RE = 6371.00877     // 지구 반경(km)
        val GRID = 5.0          // 격자 간격(km)
        val SLAT1 = 30.0        // 투영 위도1(degree)
        val SLAT2 = 60.0        // 투영 위도2(degree)
        val OLON = 126.0        // 기준점 경도(degree)
        val OLAT = 38.0         // 기준점 위도(degree)
        val XO = 43             // 기준점 X좌표(GRID)
        val YO = 136            // 기준점 Y좌표(GRID)
        val DEGRAD = Math.PI / 180.0
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = v2 * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * Math.sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * Math.cos(theta) + YO + 0.5).toInt()

        return Point(x, y)
    }

    //위, 경도 -> 주소 변환 함수
    private fun getAddress(lat: Double, lng: Double): List<Address>? {
        lateinit var address: List<Address>

        return try {
            val geocoder = Geocoder(this, Locale.KOREA)
            address = geocoder.getFromLocation(lat, lng, 1) as List<Address>
            address
        } catch (e: IOException) {
            Toast.makeText(this, "주소를 가져 올 수 없습니다", Toast.LENGTH_SHORT).show()
            null
        }
    }
}