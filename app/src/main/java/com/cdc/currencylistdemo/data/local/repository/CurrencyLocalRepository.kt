package com.cdc.currencylistdemo.data.local.repository;

import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyLocalRepository {
    suspend fun insertAll(currencyList: List<CurrencyInfoEntity>)

    fun getAllCurrency(): Flow<List<CurrencyInfoEntity>>

    fun getByType(type: String): Flow<List<CurrencyInfoEntity>>

    fun searchCrypto(query: String): Flow<List<CurrencyInfoEntity>>

    suspend fun clearAll()
}
