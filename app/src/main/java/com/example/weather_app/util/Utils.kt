package com.example.weather_app.util

import android.content.Context
import com.example.weather_app.data.model.BookmarkDataModel
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Utils {
    //CSV파일 데이터 불러오는 함수
    fun getCsvData(context: Context): MutableList<BookmarkDataModel> {
        val searchData: MutableList<BookmarkDataModel> = mutableListOf()

        try {
            val inputStream: InputStream = context.assets.open("weather_data.csv")
            val csvReader = CSVReader(InputStreamReader(inputStream, "UTF-8"))

            val allContent = csvReader.readAll() as List<Array<String>>
            for (i in 1..allContent.size - 2) {
                searchData.add(
                    BookmarkDataModel(
                        i - 1,
                        allContent[i][0],
                        allContent[i][1],
                        allContent[i][2],
                        allContent[i][3],
                        allContent[i][4],
                        allContent[i][5]
                    )
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: CsvException) {
            e.printStackTrace()
        }
        return searchData
    }

    //단기예보 BaseTime 계산 함수
    fun getBaseTime(time: LocalTime): String {
        var baseTime = ""
        if (!time.isBefore(LocalTime.of(3, 0)) && time.isBefore(LocalTime.of(6, 0))) baseTime =
            "0200"
        else if (!time.isBefore(LocalTime.of(6, 0)) && time.isBefore(
                LocalTime.of(
                    9,
                    0
                )
            )
        ) baseTime =
            "0500"
        else if (!time.isBefore(LocalTime.of(9, 0)) && time.isBefore(
                LocalTime.of(
                    12,
                    0
                )
            )
        ) baseTime = "0800"
        else if (!time.isBefore(LocalTime.of(12, 0)) && time.isBefore(
                LocalTime.of(
                    15,
                    0
                )
            )
        ) baseTime = "1100"
        else if (!time.isBefore(LocalTime.of(15, 0)) && time.isBefore(
                LocalTime.of(
                    18,
                    0
                )
            )
        ) baseTime = "1400"
        else if (!time.isBefore(LocalTime.of(18, 0)) && time.isBefore(
                LocalTime.of(
                    21,
                    0
                )
            )
        ) baseTime = "1700"
        else if (!time.isBefore(LocalTime.of(21, 0)) && time.isBefore(
                LocalTime.of(
                    23,
                    59,
                    59,
                )
            )
        ) baseTime = "2000"
        else if (!time.isBefore(LocalTime.of(0, 0)) && time.isBefore(
                LocalTime.of(
                    3,
                    0
                )
            )
        ) baseTime = "2300"

        return baseTime
    }

    //단기예보 BaseDate 계산 함수
    fun getBaseDate(time: LocalDateTime): Int {
        return if (time.hour in 0 until 3) time.minusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
        else time.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
    }
}