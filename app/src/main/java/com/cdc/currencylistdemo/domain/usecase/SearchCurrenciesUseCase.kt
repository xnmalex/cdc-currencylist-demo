package com.cdc.currencylistdemo.domain.usecase

import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchCurrenciesUseCase(
    private val repo: CurrencyLocalRepository
) {
    operator fun invoke(query: String, type:String): Flow<List<CurrencyInfo>> {
        return repo
            .getByType(type)
            .map { entityList -> entityList.map { it.toDomain() }.filter {
                        it.name.lowercase().contains(query) || it.id.lowercase()
                            .contains(query) == true
                    }
                }
            }

}