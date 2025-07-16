package com.cdc.currencylistdemo.domain.usecase

import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClearAllCurrenciesUseCase(
    private val repo: CurrencyLocalRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repo.clearAll()
    }
}