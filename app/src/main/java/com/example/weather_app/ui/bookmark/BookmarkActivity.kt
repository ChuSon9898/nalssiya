package com.example.weather_app.ui.bookmark

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.databinding.BookmarkActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkActivity : AppCompatActivity() {

    lateinit var binding: BookmarkActivityBinding
    lateinit var bookmarkEntity: BookmarkEntity

    private val listAdapter by lazy {
        BookmarkListAdapter()
    }

    private val bookmarkViewModel by lazy {
        ViewModelProvider(this).get(BookmarkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BookmarkActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
        initModel()
    }



    private fun initView() = with(binding) {

        var itemTouchHelper = ItemTouchHelper(SwipeToDelete(listAdapter, this@BookmarkActivity))
        itemTouchHelper.attachToRecyclerView(bookmarkRv)

        bookmarkRv.adapter = listAdapter
        bookmarkRv.layoutManager = LinearLayoutManager(this@BookmarkActivity)

        //추가 코드 -> 수정 예정
        bookmarkIbSearch.setOnClickListener {
            val location = bookmarkEtSearch.text.toString()
            bookmarkViewModel.insertData(location)
            bookmarkEtSearch.setText("")
        }

    }

    private fun initModel() = with(bookmarkViewModel) {
        bookmarkViewModel.bookmarkList.observe(this@BookmarkActivity){ bookmarkList ->
            listAdapter.submitList(bookmarkList)
        }
    }
}