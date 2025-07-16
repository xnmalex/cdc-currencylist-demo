package com.cdc.currencylistdemo.data.local.repository;

import com.cdc.currencylistdemo.data.local.dao.CurrencyDao
import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import kotlinx.coroutines.flow.Flow

class CurrencyLocalRepositoryImpl(private val currencyDao: CurrencyDao): CurrencyLocalRepository {
    override suspend fun insertAll(currencyList: List<CurrencyInfoEntity>) {
        currencyDao.insertAll(currencyList)
    }

    override fun getAllCurrency(): Flow<List<CurrencyInfoEntity>> = currencyDao.getAll()

    override fun getByType(type: String): Flow<List<CurrencyInfoEntity>> = currencyDao.getByType(type)

    override fun searchCurrency(query: String, type: String): Flow<List<CurrencyInfoEntity>> = currencyDao.searchCurrency(query, type)

    override suspend fun clearAll() {
        currencyDao.clearAll()
    }
}
