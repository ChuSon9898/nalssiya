package com.example.weather_app.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.HomeActivityBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeActivityBinding

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private val hourlyAdapter = HourlyListAdapter()
    private val dailyAdapter = DailyListAdapter()

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
        hourlyList.observe(this@HomeActivity, Observer {
            hourlyAdapter.submitList(it)
        })
        dailyList.observe(this@HomeActivity, Observer {
            dailyAdapter.submitList(it)
        })
    }
}