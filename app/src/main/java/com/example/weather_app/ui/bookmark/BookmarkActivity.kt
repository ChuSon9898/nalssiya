package com.example.weather_app.ui.bookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.BookmarkActivityBinding

class BookmarkActivity : AppCompatActivity() {

    lateinit var binding: BookmarkActivityBinding

    private val listAdapter by lazy {
        BookmarkListAdapter()
    }

    private val favoriteViewModel by lazy {
        ViewModelProvider(this, FavoriteViewModelFactory()).get(FavoriteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BookmarkActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initModel()
    }

    private fun initView() = with(binding) {

        favoriteRv.adapter = listAdapter
        favoriteRv.layoutManager = LinearLayoutManager(this@BookmarkActivity)

    }

    private fun initModel() = with(favoriteViewModel) {
        favoriteViewModel.favoriteList.observe(this@BookmarkActivity){ favoriteList ->
            listAdapter.submitList(favoriteList)
        }
    }
}