package com.example.weather_app.util

import android.content.Context
import com.example.weather_app.data.model.BookmarkDataModel
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object Utils {
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
}