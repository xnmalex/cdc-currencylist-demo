package com.cdc.currencylistdemo.di

import AppDatabase
import androidx.room.Room
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepositoryImpl
import com.cdc.currencylistdemo.data.source.AssetDataSource
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "currency_db").build() }
    single { get<AppDatabase>().currencyDao() }
    single { AssetDataSource(get()) }
    single<CurrencyLocalRepository> { CurrencyLocalRepositoryImpl(get()) }

}