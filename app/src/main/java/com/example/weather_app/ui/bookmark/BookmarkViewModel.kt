package com.example.weather_app.ui.bookmark

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.model.SearchLocation
import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.data.room.Repository
import com.example.weather_app.databinding.BookmarkActivityBinding
import com.example.weather_app.databinding.BookmarkRvItemBinding
import com.example.weather_app.databinding.BookmarkSearchRvItemBinding
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import kotlinx.coroutines.CoroutineScope
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

    private val _searchList = MutableLiveData<MutableList<SearchLocation>>()
    val searchList: LiveData<MutableList<SearchLocation>>
        get() = _searchList

    val totalSearchList : MutableList<SearchLocation> = mutableListOf()

    val repository = Repository(context)

    init {
        getAllData()
        getSearchData()
    }

    fun getAllData() = viewModelScope.launch(Dispatchers.IO) {

        val result = repository.getListAll()

        val bookmarkList : MutableList<BookmarkDataModel> = mutableListOf()

        for(r in 0 .. result.size -1 ){

            if(r == 0){
                bookmarkList.add(BookmarkDataModel(result[r].id, result[r].location, "60", "127", "11B00000", "11B10101"))
            }else{
                bookmarkList.add(BookmarkDataModel(result[r].id, result[r].location, "60", "127", "11B00000", "11B10101"))
            }
        }

        _bookmarkList.postValue(bookmarkList)
    }

    fun getSearchData(){

        var searchData : MutableList<SearchLocation> = mutableListOf()

        try {
            val inputStream: InputStream = context.assets.open("weather_data.csv")
            val csvReader = CSVReader(InputStreamReader(inputStream, "UTF-8"))

            val allContent = csvReader.readAll() as List<Array<String>>
            for (i in 1 .. allContent.size-2) {
                searchData.add(SearchLocation(i-1, allContent[i][0], allContent[i][1], allContent[i][2], allContent[i][3], allContent[i][4], allContent[i][5]))
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
            val filteredList = mutableListOf<SearchLocation>()

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
