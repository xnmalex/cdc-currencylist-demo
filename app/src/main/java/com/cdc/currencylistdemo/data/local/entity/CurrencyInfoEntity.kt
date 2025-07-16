package com.cdc.currencylistdemo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyInfoEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val code: String?,
    val type: String
)