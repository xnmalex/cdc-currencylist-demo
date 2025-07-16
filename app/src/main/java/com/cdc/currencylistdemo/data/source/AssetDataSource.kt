package com.cdc.currencylistdemo.data.source

import android.content.Context
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AssetDataSource(private val context: Context) {
    fun loadCurrenciesFromAsset(fileName: String): List<CurrencyInfo> {
        return try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<CurrencyInfo>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}