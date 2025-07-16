package com.cdc.currencylistdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.domain.usecase.ClearAllCurrenciesUseCase
import com.cdc.currencylistdemo.domain.usecase.GetAllPurchasableCurrenciesUseCase
import com.cdc.currencylistdemo.domain.usecase.GetCurrenciesByTypeUseCase
import com.cdc.currencylistdemo.domain.usecase.LoadCurrenciesFromAssetUseCase
import com.cdc.currencylistdemo.domain.usecase.SearchCurrenciesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CurrencyViewModel (
    private val clearAllCurrenciesUseCase: ClearAllCurrenciesUseCase,
    private val loadCurrenciesFromAssetUseCase: LoadCurrenciesFromAssetUseCase,
    private val getCurrenciesByTypeUseCase: GetCurrenciesByTypeUseCase,
    private val getAllPurchasableCurrenciesUseCase: GetAllPurchasableCurrenciesUseCase,
    private val searchCurrenciesUseCase : SearchCurrenciesUseCase

) : ViewModel(){

    // Event flow for one-time messages
    private val _msgFlow = MutableSharedFlow<String>()
    val msgFlow: SharedFlow<String> = _msgFlow.asSharedFlow()

    private var currentList: List<CurrencyInfo> = emptyList()

    private val _currencyListFlow = MutableSharedFlow<List<CurrencyInfo>>()
    val currencyListFlow = _currencyListFlow.asSharedFlow()

    private val _suggestionFlow = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val suggestionFlow = _suggestionFlow.asStateFlow()

    /** Preload data from asset JSON into Room database */
    fun loadCurrenciesFromAsset() {
        viewModelScope.launch {
            try {
                loadCurrenciesFromAssetUseCase("crypto.json", "crypto")
                loadCurrenciesFromAssetUseCase("fiat.json", "fiat")
                _msgFlow.emit("Insert into database successfully")
            } catch (e: Exception) {
                _msgFlow.emit("Failed to preload JSON: ${e.message}")
            }
        }
    }

    fun loadCurrencyByType(type: String) {
        viewModelScope.launch {
            getCurrenciesByTypeUseCase(type).catch { e ->
                _msgFlow.emit(e.message ?: "Unknown error")
            }.collect { list ->
                currentList = list
                _currencyListFlow.emit(list)
            }
        }
    }

    /** Search the current list */
    fun searchCurrencies(query: String, type: String) {
        viewModelScope.launch {
            try {
                searchCurrenciesUseCase(query, type).collect { list ->
                    _suggestionFlow.value = list
                }
            } catch (e: Exception) {
                _msgFlow.emit(e.message ?: "Unknown error")
            }
        }
    }

    fun clearCurrencies() {
        viewModelScope.launch {
            try {
                clearAllCurrenciesUseCase()
                currentList = emptyList()

                _msgFlow.emit("Database cleared successfully")
            } catch (e: Exception) {
                _msgFlow.emit(e.message ?: "Error clearing currencies")
            }
        }
    }

    fun getAllPurchasableCurrencies() {
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    getAllPurchasableCurrenciesUseCase()
                        .collect { list ->
                            currentList = list
                            _currencyListFlow.emit(list)
                        }
                } catch (e: Exception) {
                    _msgFlow.emit(e.message ?: "Unknown error")
                }
            }
        }
    }
}