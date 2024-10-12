package com.animetoon.app.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.animetoon.app.data.model.Webtoon
import com.animetoon.app.utils.Result
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.DecimalFormat

class MainRepository(private val context: Context) {

    suspend fun getWebtoonsFromJson(): Result<List<Webtoon>> {
        return try {
            val inputStream = context.assets.open("webtoon_data.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }

            val webtoonListType = object : TypeToken<List<Webtoon>>() {}.type
            val webtoons: List<Webtoon> = Gson().fromJson(jsonString, webtoonListType)
            delay(1000)
            Result.Success(webtoons)
        } catch (e: Exception) {
            Result.Error(e.message ?: "An error occurred while reading the JSON file")
        }
    }


    suspend fun updateJsonData(context: Context, updatedWebtoon: Webtoon): Result<Boolean> {
        return try {
            val jsonFileName = "webtoon_data.json"
            val inputStream = context.assets.open(jsonFileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }

            val webtoonListType = object : TypeToken<List<Webtoon>>() {}.type
            val webtoons: MutableList<Webtoon> = Gson().fromJson(jsonString, webtoonListType)

            val webtoonIndex = webtoons.indexOfFirst { it.id == updatedWebtoon.id }
            if (webtoonIndex != -1) {

                val formattedRating = DecimalFormat("#.#").format(updatedWebtoon.averageRating)
                updatedWebtoon.averageRating = formattedRating.toDouble() // Update the webtoon data
                webtoons[webtoonIndex] = updatedWebtoon // Update the webtoon data
            }


            val jsonFile = File(context.filesDir, jsonFileName)
            OutputStreamWriter(jsonFile.outputStream()).use { writer ->
                writer.write(Gson().toJson(webtoons))
            }

            Result.Success(true)
        } catch (e: Exception) {
            Log.e("Error", e.message ?: "An error occurred while updating the JSON file")
            Result.Error(e.message ?: "An error occurred while updating the JSON file")
        }
    }


}
