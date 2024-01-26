package com.example.weather_app.ui.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.data.room.Repository
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class BookmarkViewModel(application: Application) : AndroidViewModel(application){

    val context = getApplication<Application>().applicationContext
    val db = BookmarkDatabase.getDatabase(context)

    private val _bookmarkList = MutableLiveData<MutableList<BookmarkDataModel>>()
    val bookmarkList: LiveData<MutableList<BookmarkDataModel>>
        get() = _bookmarkList

    private val _searchList = MutableLiveData<MutableList<BookmarkDataModel>>()
    val searchList: LiveData<MutableList<BookmarkDataModel>>
        get() = _searchList

    val totalSearchList : MutableList<BookmarkDataModel> = mutableListOf()

    val repository = Repository(context)

    init {
        getAllData()
        getSearchData()
    }

    fun getAllData() = viewModelScope.launch(Dispatchers.IO) {

        val result = repository.getListAll()

        val bookmarkList : MutableList<BookmarkDataModel> = mutableListOf()

        for(r in 0 .. result.size -1 ){

            bookmarkList.add(BookmarkDataModel(result[r].id, result[r].location.slice(0..result[r].location.indexOf(" ")-1), result[r].location.slice(result[r].location.indexOf(" ")+1..result[r].location.length-1),"60", "127", "11B00000", "11B10101"))

        }

        _bookmarkList.postValue(bookmarkList)
    }

    fun getSearchData(){

        var searchData : MutableList<BookmarkDataModel> = mutableListOf()

        try {
            val inputStream: InputStream = context.assets.open("weather_data.csv")
            val csvReader = CSVReader(InputStreamReader(inputStream, "UTF-8"))

            val allContent = csvReader.readAll() as List<Array<String>>
            for (i in 1 .. allContent.size-2) {
                searchData.add(BookmarkDataModel(i-1, allContent[i][0], allContent[i][1], allContent[i][2], allContent[i][3], allContent[i][4], allContent[i][5]))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: CsvException) {
            e.printStackTrace()
        }

        totalSearchList.clear()
        _searchList.postValue(searchData)
        totalSearchList.addAll(searchData)
    }

    fun searchLocation(s: CharSequence?){
        if (s == null || s.isEmpty()) {

            _searchList.value = totalSearchList

        } else {
            val filteredList = mutableListOf<BookmarkDataModel>()

            if (totalSearchList != null) {
                for (item in totalSearchList) {
                    val location = item.Gu + " " + item.Dong
                    if (item.Gu.contains(s, ignoreCase = true) || item.Dong.contains(s, true) || location.contains(s, true)) {
                        filteredList.add(item)
                    }
                }
            }

            _searchList.value = filteredList

        }

    }

    fun insertData(location: String, nx: String, ny: String, landArea: String, tempArea: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(location, nx, ny, landArea, tempArea)
        getAllData()
    }

    fun deleteData(id: Int, location: String, nx: String, ny: String, landArea: String, tempArea: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteData(id, location, nx, ny, landArea, tempArea)
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