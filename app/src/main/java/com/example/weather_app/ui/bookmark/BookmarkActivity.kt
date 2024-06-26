package com.example.weather_app.ui.bookmark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.databinding.BookmarkActivityBinding
import com.example.weather_app.ui.bookmark.BookmarkDetailActivity.Companion.detailIntent
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: BookmarkActivityBinding

    private val listAdapter by lazy {
        BookmarkListAdapter()
    }

    private val searchListAdapter by lazy {
        BookmarkSearchListAdapter()
    }

    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    private val addBoardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val item = result.data?.getParcelableExtra("newBookmark", BookmarkDataModel::class.java)
                    if (item != null) {
                        bookmarkViewModel.insertData(item.Gu + " " + item.Dong, item.nx, item.ny, item.landArea, item.tempArea)
                    }
                } else {
                    val item = result.data?.getParcelableExtra<BookmarkDataModel>("newBookmark")
                    if (item != null) {
                        bookmarkViewModel.insertData(item.Gu + " " + item.Dong, item.nx, item.ny, item.landArea, item.tempArea)
                    }
                }
            }
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
        initViewModel()
    }

    private fun initView() = with(binding) {
        //스와이프 코드
        var itemTouchHelper = ItemTouchHelper(SwipeToDelete(listAdapter, this@BookmarkActivity))
        itemTouchHelper.attachToRecyclerView(bookmarkRv)

        ivBack.setOnClickListener {
            finish()
        }

        bookmarkRv.adapter = listAdapter
        bookmarkRv.layoutManager = LinearLayoutManager(this@BookmarkActivity)

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
            override fun onItemClick(item: BookmarkDataModel, position: Int) {
                addBoardLauncher.launch(detailIntent(this@BookmarkActivity, item))
                bookmarkSearchbar.closeSearch()
            }
        })

        listAdapter.setOnItemClickListener(object : BookmarkListAdapter.OnItemClickListener{
            override fun onItemClick(item: BookmarkDataModel, position: Int) {
                addBoardLauncher.launch(detailIntent(this@BookmarkActivity, item))
            }
        })

    }

    private fun initViewModel() = with(bookmarkViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    bookmarkList.collectLatest {
                        listAdapter.submitList(it)
                    }
                }
            }
        }
        bookmarkViewModel.searchList.observe(this@BookmarkActivity){ searchList ->
            searchListAdapter.submitList(searchList)
        }
    }
}