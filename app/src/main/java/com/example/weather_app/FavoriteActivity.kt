package com.example.weather_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.FavoriteActivityBinding

class FavoriteActivity : AppCompatActivity() {

    lateinit var binding: FavoriteActivityBinding

    private val listAdapter by lazy {
        FavoriteListAdapter()
    }

    private val favoriteViewModel by lazy {
        ViewModelProvider(this, FavoriteViewModelFactory()).get(FavoriteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FavoriteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initModel()
    }

    private fun initView() = with(binding) {

        favoriteRv.adapter = listAdapter
        favoriteRv.layoutManager = LinearLayoutManager(this@FavoriteActivity)

    }

    private fun initModel() = with(favoriteViewModel) {
        favoriteViewModel.favoriteList.observe(this@FavoriteActivity){ favoriteList ->
            listAdapter.submitList(favoriteList)
        }
    }
}