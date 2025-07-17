package com.cdc.currencylistdemo.domain.usecase

import com.cdc.currencylistdemo.CurrencyConstants
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchCurrenciesUseCase(
    private val repo: CurrencyLocalRepository
) {
    operator fun invoke(query: String, type: String): Flow<List<CurrencyInfo>> {
        val baseFlow = if (type.isBlank()) {
            repo.getAllCurrency()
        } else {
            repo.getByType(type)
        }
        return baseFlow
            .map { entityList ->
                entityList.map { it.toDomain() }.filter {
                    val lowerQuery = query.trim().lowercase()

                    val name = it.name.lowercase()
                    val symbolOrCode = when (type.lowercase()) {
                        CurrencyConstants.TYPE_CRYPTO -> it.symbol.lowercase()
                        CurrencyConstants.TYPE_FIAT -> it.code?.lowercase()
                        else -> ""
                    }

                    val nameStartsWith = name.startsWith(lowerQuery)
                    val nameContainsWithSpace = name.contains(" $lowerQuery")
                    val symbolOrCodeStartsWith = symbolOrCode?.startsWith(lowerQuery)

                    nameStartsWith || nameContainsWithSpace || symbolOrCodeStartsWith == true
                }
            }
    }
}
