package com.example.weather_app.ui.bookmark

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.model.BookmarkDataModel
import com.example.weather_app.data.repository.retrofit.HourlyRepositoryImpl
import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.data.repository.room.BookmarkRepositoryImpl
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val bookmarkRepository : BookmarkRepositoryImpl, private val hourlyRepository: HourlyRepositoryImpl, private val bookmarkDatabase: BookmarkDatabase, application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _bookmarkList = MutableStateFlow<List<BookmarkDataModel>>(emptyList())
    val bookmarkList: StateFlow<List<BookmarkDataModel>> get() = _bookmarkList

    private val _searchList = MutableLiveData<MutableList<BookmarkDataModel>>()
    val searchList: LiveData<MutableList<BookmarkDataModel>> get() = _searchList

    private val totalSearchList: MutableList<BookmarkDataModel> = mutableListOf()

    init {
        getAllData()
        getSearchData()
    }

    //Room 데이터 전체 가져오기
    fun getAllData() = viewModelScope.launch(Dispatchers.IO) {
        bookmarkRepository.getListAll().collectLatest { result ->
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
                        result[r].tempArea,
                    )
                )
            }

            val deferredList = bookmarkList.map { item ->
                async(Dispatchers.IO) {
                    val responseMinMax = hourlyRepository.getHourlyData(
                        200,
                        1,
                        LocalDateTime.now().minusDays(1)
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt(),
                        "2300",
                        item.nx,
                        item.ny
                    )
                    val minMaxList = responseMinMax.body()?.response!!.body.items.item

                    //최저, 최고 온도
                    val minTemp = minMaxList.firstOrNull { it.category == "TMN" }?.fcstValue ?: ""
                    val maxTemp = minMaxList.firstOrNull { it.category == "TMX" }?.fcstValue ?: ""

                    val responseTemp = hourlyRepository.getHourlyData(
                        100,
                        1,
                        Utils.getBaseDate(LocalDateTime.now()),
                        Utils.getBaseTime(LocalTime.now()),
                        item.nx,
                        item.ny
                    )
                    val tempList = responseTemp.body()?.response!!.body.items.item

                    //현재 온도
                    val temp = tempList.firstOrNull {
                        it.category == "TMP" && it.fcstTime == "${
                            LocalTime.now().format(DateTimeFormatter.ofPattern("HH"))
                        }00"
                    }?.fcstValue ?: ""

                    item.apply {
                        this.temp = temp
                        this.minTemp = minTemp
                        this.maxTemp = maxTemp
                    }
                }
            }
            val newBookmarkList = deferredList.awaitAll()

            _bookmarkList.value = newBookmarkList
        }
    }

    fun getDataByLocation (item : BookmarkDataModel) : List<BookmarkEntity> {
        return bookmarkRepository.getDataByLocation(item.Gu + " " + item.Dong)
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

            _searchList.value = filteredList

        }

    }

    //Room 데이터 넣는 함수
    fun insertData(location: String, nx: String, ny: String, landArea: String, tempArea: String) =
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkRepository.insertData(location, nx, ny, landArea, tempArea)
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
    }
}