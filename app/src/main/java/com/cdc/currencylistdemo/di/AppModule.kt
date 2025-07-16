package com.cdc.currencylistdemo.di

import AppDatabase
import androidx.room.Room
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepositoryImpl
import com.cdc.currencylistdemo.data.source.AssetDataSource
import com.cdc.currencylistdemo.domain.usecase.ClearAllCurrenciesUseCase
import com.cdc.currencylistdemo.domain.usecase.GetAllPurchasableCurrenciesUseCase
import com.cdc.currencylistdemo.domain.usecase.GetCurrenciesByTypeUseCase
import com.cdc.currencylistdemo.domain.usecase.LoadCurrenciesFromAssetUseCase
import com.cdc.currencylistdemo.domain.usecase.SearchCurrenciesUseCase
import com.cdc.currencylistdemo.ui.viewmodel.CurrencyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "currency_db").build() }
    single { get<AppDatabase>().currencyDao() }
    single { AssetDataSource(get()) }
    single<CurrencyLocalRepository> { CurrencyLocalRepositoryImpl(get()) }

    // ViewModel
    viewModel {
        CurrencyViewModel(
            clearAllCurrenciesUseCase = get(),
            loadCurrenciesFromAssetUseCase = get(),
            getCurrenciesByTypeUseCase = get(),
            getAllPurchasableCurrenciesUseCase = get(),
            searchCurrenciesUseCase = get()
        )
    }

    // Use Cases
    single { ClearAllCurrenciesUseCase(get()) }
    single { LoadCurrenciesFromAssetUseCase(get(), get()) }
    single { GetCurrenciesByTypeUseCase(get()) }
    single { SearchCurrenciesUseCase(get()) }
    single { GetAllPurchasableCurrenciesUseCase(get()) }

}