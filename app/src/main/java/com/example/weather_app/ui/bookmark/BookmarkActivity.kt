package com.example.weather_app.ui.bookmark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.data.model.SearchLocation
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.databinding.BookmarkActivityBinding
import com.example.weather_app.ui.bookmark.BookmarkDetailActivity.Companion.detailIntent
import com.mancj.materialsearchbar.MaterialSearchBar

class BookmarkActivity : AppCompatActivity() {

    lateinit var binding: BookmarkActivityBinding
    lateinit var bookmarkEntity: BookmarkEntity

    private val listAdapter by lazy {
        BookmarkListAdapter()
    }

    private val searchListAdapter by lazy {
        BookmarkSearchListAdapter()
    }

    private val bookmarkViewModel by lazy {
        ViewModelProvider(this).get(BookmarkViewModel::class.java)
    }

    companion object {
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

        var itemTouchHelper = ItemTouchHelper(SwipeToDelete(listAdapter, this@BookmarkActivity))
        itemTouchHelper.attachToRecyclerView(bookmarkRv)

        bookmarkRv.adapter = listAdapter
        bookmarkRv.layoutManager = LinearLayoutManager(this@BookmarkActivity)

//        //추가 코드 -> 수정 예정
//        bookmarkIbSearch.setOnClickListener {
//            val location = bookmarkEtSearch.text.toString()
//            bookmarkViewModel.insertData(location)
//            bookmarkEtSearch.setText("")
//        }

        bookmarkSearchbar.setHint("도시 또는 공항 검색")
        bookmarkSearchbar.setSpeechMode(false)

        bookmarkSearchRv.adapter = searchListAdapter
        bookmarkSearchRv.layoutManager = LinearLayoutManager(this@BookmarkActivity)
        bookmarkSearchRv.addItemDecoration(DividerItemDecoration(this@BookmarkActivity, LinearLayout.VERTICAL))


        bookmarkSearchbar.setOnSearchActionListener(object: MaterialSearchBar.OnSearchActionListener{
            override fun onSearchStateChanged(enabled: Boolean) {
                if(enabled){
                    bookmarkSearchRv.visibility = View.VISIBLE
                    bookmarkRv.visibility = View.INVISIBLE
                    (bookmarkSearchRv.layoutManager as LinearLayoutManager).scrollToPosition(0)
                }else{
                    bookmarkSearchRv.visibility = View.INVISIBLE
                    bookmarkRv.visibility = View.VISIBLE
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {

            }

            override fun onButtonClicked(buttonCode: Int) {
                Log.d("Button ID", buttonCode.toString())

            }

        })

        bookmarkSearchbar.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bookmarkViewModel.searchLocation(s)
            }

        })

        searchListAdapter.setOnItemClickListener(object : BookmarkSearchListAdapter.OnItemClickListener{
            override fun onItemClick(item: SearchLocation, position: Int) {
                Toast.makeText(this@BookmarkActivity, item.toString(), Toast.LENGTH_SHORT).show()

                startActivity(detailIntent(this@BookmarkActivity, item))

                bookmarkSearchbar.closeSearch()
            }
        })
    }

    private fun initModel() = with(bookmarkViewModel) {
        bookmarkViewModel.bookmarkList.observe(this@BookmarkActivity){ bookmarkList ->
            listAdapter.submitList(bookmarkList)
        }

        bookmarkViewModel.searchList.observe(this@BookmarkActivity){ searchList ->
            searchListAdapter.submitList(searchList)
        }
    }
}