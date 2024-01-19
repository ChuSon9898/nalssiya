package com.example.weather_app.ui.bookmark

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.data.room.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookmarkViewModel(application: Application) : AndroidViewModel(application){

    val context = getApplication<Application>().applicationContext
    val db = BookmarkDatabase.getDatabase(context)

    private val _bookmarkList = MutableLiveData<MutableList<BookmarkDataModel>>()
    val bookmarkList: LiveData<MutableList<BookmarkDataModel>>
        get() = _bookmarkList

    val repository = Repository(context)

    init {
        getAllData()
    }

    fun getAllData() = viewModelScope.launch(Dispatchers.IO) {

        val result = repository.getListAll()

        val bookmarkList : MutableList<BookmarkDataModel> = mutableListOf()

        for(r in 0 .. result.size -1 ){

            if(r == 0){
                bookmarkList.add(BookmarkDataModel(result[r].id, result[r].location, "서울특별시", "-8°", "최고 -2°  최저 -8°"))
            }else{
                bookmarkList.add(BookmarkDataModel(result[r].id, result[r].location, "오후 16:00", "-8°", "최고 -2°  최저 -8°"))
            }
        }

        _bookmarkList.postValue(bookmarkList)
    }

    fun insertData(location: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(location)
        getAllData()
    }

    fun deleteAllData() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllData()
        getAllData()
    }

    fun deleteData(id: Int, location: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteData(id, location)
        getAllData()
    }

}
