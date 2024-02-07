package com.example.weather_app.ui.bookmark

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.room.BookmarkRepository
import com.example.weather_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    private val _bookmarkList = MutableLiveData<MutableList<BookmarkDataModel>>()
    val bookmarkList: LiveData<MutableList<BookmarkDataModel>>
        get() = _bookmarkList

    private val _searchList = MutableLiveData<MutableList<BookmarkDataModel>>()
    val searchList: LiveData<MutableList<BookmarkDataModel>>
        get() = _searchList

    val totalSearchList: MutableList<BookmarkDataModel> = mutableListOf()

    val bookmarkRepository = BookmarkRepository(context)

    init {
        getAllData()
        getSearchData()
    }

    //Room 데이터 전체 가져오기
    fun getAllData() = viewModelScope.launch(Dispatchers.IO) {

        val result = bookmarkRepository.getListAll()

        val bookmarkList: MutableList<BookmarkDataModel> = mutableListOf()

        for (r in 0..result.size - 1) {

            bookmarkList.add(
                BookmarkDataModel(
                    result[r].id,
                    result[r].location.slice(0..result[r].location.indexOf(" ") - 1),
                    result[r].location.slice(result[r].location.indexOf(" ") + 1..result[r].location.length - 1),
                    result[r].nx,
                    result[r].ny,
                    result[r].landArea,
                    result[r].tempArea
                )
            )

        }

        _bookmarkList.postValue(bookmarkList)
    }

    //weatherdata 파일에서 데이터 가져오기
    fun getSearchData() {
        totalSearchList.clear()

        _searchList.postValue(Utils.getCsvData(context))
        totalSearchList.addAll(Utils.getCsvData(context))
    }

    //검색 데이터 서치하는 함수
    fun searchLocation(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            _searchList.value = totalSearchList

        } else {
            val filteredList = mutableListOf<BookmarkDataModel>()

            if (totalSearchList != null) {
                for (item in totalSearchList) {
                    val location = item.Gu + " " + item.Dong
                    if (item.Gu.contains(s, ignoreCase = true) || item.Dong.contains(
                            s,
                            true
                        ) || location.contains(s, true)
                    ) {
                        filteredList.add(item)
                    }
                }
            }

            _searchList.value = filteredList

        }

    }

    //Room 데이터 넣는 함수
    fun insertData(location: String, nx: String, ny: String, landArea: String, tempArea: String) =
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkRepository.insertData(location, nx, ny, landArea, tempArea)
            getAllData()
        }

    //Room 데이터 삭제하는 함수
    fun deleteData(
        id: Int,
        location: String,
        nx: String,
        ny: String,
        landArea: String,
        tempArea: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        bookmarkRepository.deleteData(id, location, nx, ny, landArea, tempArea)
        getAllData()
    }

}

class BookmarkViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(application) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}