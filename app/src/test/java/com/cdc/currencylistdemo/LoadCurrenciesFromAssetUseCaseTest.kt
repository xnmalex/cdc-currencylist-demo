package com.cdc.currencylistdemo

import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.data.source.AssetDataSource
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.domain.model.toEntity
import com.cdc.currencylistdemo.domain.usecase.LoadCurrenciesFromAssetUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoadCurrenciesFromAssetUseCaseTest {
    private val assetLoader = mock<AssetDataSource>()
    private val repo = mock<CurrencyLocalRepository>()
    private lateinit var useCase: LoadCurrenciesFromAssetUseCase

    @Before
    fun setup() {
        useCase = LoadCurrenciesFromAssetUseCase(assetLoader, repo)
    }

    @Test
    fun `loads and inserts mapped currencies`() = runTest {
        val domainList = listOf(
            CurrencyInfo("1", "BTC", "BTC", "btc", "crypto")
        )
        val expectedEntities = domainList.map { it.copy(type = "crypto").toEntity(type = "crypto") }

        whenever(assetLoader.loadCurrenciesFromAsset("crypto.json")).thenReturn(domainList)

        useCase("crypto.json", "crypto")

        verify(repo).insertAll(expectedEntities)
    }

    @Test
    fun `throws when asset loader fails`() = runTest {
        val errorMessage = "Asset load error"
        whenever(assetLoader.loadCurrenciesFromAsset("crypto.json")).thenThrow(RuntimeException(errorMessage))

        val exception = assertFailsWith<RuntimeException> {
            useCase("crypto.json", "crypto")
        }

        assertEquals(errorMessage, exception.message)
    }
}