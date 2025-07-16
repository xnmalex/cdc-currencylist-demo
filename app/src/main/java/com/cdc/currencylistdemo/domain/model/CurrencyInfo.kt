package com.cdc.currencylistdemo.domain.model

import android.os.Parcelable
import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyInfo(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String?,
    val type: String // "crypto" or "fiat"
) : Parcelable {
    override fun toString(): String = "$name ($id)"
}

fun CurrencyInfo.toEntity(type:String): CurrencyInfoEntity {
    return CurrencyInfoEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        code = this.code,
        type = type,
    )
}

fun CurrencyInfoEntity.toDomain(): CurrencyInfo {
    return CurrencyInfo(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        code = this.code,
        type = this.type,
    )
}