package com.example.weather_app.ui.bookmark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.databinding.BookmarkActivityBinding

class BookmarkActivity : AppCompatActivity() {

    lateinit var binding: BookmarkActivityBinding
    lateinit var bookmarkEntity: BookmarkEntity

    private val listAdapter by lazy {
        BookmarkListAdapter()
    }

    private val bookmarkViewModel by lazy {
        ViewModelProvider(this).get(BookmarkViewModel::class.java)
    }

    companion object {
        const val OBJECT_DATA = "item_object"
        fun bookmarkIndent(context: Context): Intent {
            val intent = Intent(context, BookmarkActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BookmarkActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
        initModel()
    }

    private fun initView() = with(binding) {

        bookmarkRv.adapter = listAdapter
        bookmarkRv.layoutManager = LinearLayoutManager(this@BookmarkActivity)

        //추가 코드 -> 수정 예정
        bookmarkIbSearch.setOnClickListener {
            val location = bookmarkEtSearch.text.toString()
            bookmarkViewModel.insertData(location)
            bookmarkEtSearch.setText("")
        }

        //삭제 코드 -> 수정 예정
        listAdapter.setOnItemClickListener(object : BookmarkListAdapter.OnItemClickListener{
            override fun onItemClick(item: BookmarkDataModel, position: Int) {
                bookmarkViewModel.deleteData(item.id, item.location)
            }
        })

    }

    private fun initModel() = with(bookmarkViewModel) {
        bookmarkViewModel.bookmarkList.observe(this@BookmarkActivity){ bookmarkList ->
            listAdapter.submitList(bookmarkList)
        }
    }
}