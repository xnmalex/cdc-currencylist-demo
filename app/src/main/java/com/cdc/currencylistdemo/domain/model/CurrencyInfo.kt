package com.cdc.currencylistdemo.domain.model

data class CurrencyInfo(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String,
    val type: String // "crypto" or "fiat"
)
