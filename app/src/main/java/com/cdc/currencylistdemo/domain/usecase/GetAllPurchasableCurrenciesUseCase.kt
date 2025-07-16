package com.cdc.currencylistdemo.domain.usecase

import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllPurchasableCurrenciesUseCase(
    private val repo: CurrencyLocalRepository
) {
     operator fun invoke(): Flow<List<CurrencyInfo>> {
        return repo.getAllCurrency()
            .map { entityList ->
                entityList.map { it.toDomain() }
            }
    }
}