package com.example.weather_app.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.HomeActivityBinding
import com.example.weather_app.ui.bookmark.BookmarkListAdapter

class HomeActivity : AppCompatActivity() {
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

        rvHourly.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, true)
        rvDaily.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
    }

    private fun initViewModel() = with(viewModel) {
        hourlyList.observe(this@HomeActivity) { hourlyList ->
            hourlyAdapter.submitList(hourlyList)
        }
        dailyList.observe(this@HomeActivity) { dailyList ->
            dailyAdapter.submitList(dailyList)
        }
    }
}