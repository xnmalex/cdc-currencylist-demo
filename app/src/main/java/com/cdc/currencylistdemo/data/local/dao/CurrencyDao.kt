package com.cdc.currencylistdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyInfoEntity>)

    @Query("SELECT * FROM currency")
    fun getAll(): Flow<List<CurrencyInfoEntity>>

    @Query("SELECT * FROM currency WHERE type = :type")
    fun getByType(type: String): Flow<List<CurrencyInfoEntity>>

    @Query("SELECT * FROM currency WHERE type = 'crypto' AND name LIKE '%' || :query || '%'")
    fun searchCrypto(query: String): Flow<List<CurrencyInfoEntity>>

    @Query("DELETE FROM currency")
    suspend fun clearAll()
}