package com.cdc.currencylistdemo.domain.usecase

import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.data.source.AssetDataSource
import com.cdc.currencylistdemo.domain.model.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadCurrenciesFromAssetUseCase(
    private val assetLoader: AssetDataSource,
    private val repo: CurrencyLocalRepository
) {
    suspend operator fun invoke(fileName: String, type:String) = withContext(Dispatchers.IO) {
        val currencies = assetLoader.loadCurrenciesFromAsset(fileName)
        val entityList: List<CurrencyInfoEntity> = currencies.map { it.toEntity(type) }
        repo.insertAll(entityList)
    }
}