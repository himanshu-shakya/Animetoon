package com.animetoon.app.data.repository
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.animetoon.app.data.model.Webtoon
import com.animetoon.app.utils.Result
import java.io.BufferedReader
import java.io.InputStreamReader

class MainRepository(private val context: Context) {

    fun getWebtoonsFromJson(): Result<List<Webtoon>> {
        return try {
            // Read JSON file from assets
            val inputStream = context.assets.open("webtoon_data.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }

            // Parse JSON to List<Webtoon>
            val webtoonListType = object : TypeToken<List<Webtoon>>() {}.type
            val webtoons: List<Webtoon> = Gson().fromJson(jsonString, webtoonListType)

            Result.Success(webtoons)
        } catch (e: Exception) {
            Result.Error(e.message ?: "An error occurred while reading the JSON file")
        }
    }
}
