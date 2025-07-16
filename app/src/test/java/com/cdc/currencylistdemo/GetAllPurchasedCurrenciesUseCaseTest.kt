package com.cdc.currencylistdemo

import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.domain.model.toDomain
import com.cdc.currencylistdemo.domain.usecase.GetAllPurchasableCurrenciesUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertFailsWith

class GetAllPurchaseableCurrenciesUseCaseTest {
    private val repo = mock<CurrencyLocalRepository>()
    private lateinit var useCase: GetAllPurchasableCurrenciesUseCase

    @Before
    fun setup() {
        useCase = GetAllPurchasableCurrenciesUseCase(repo)
    }

    @Test
    fun `returns purchased currencies`() = runTest {
        val entities = listOf(
            CurrencyInfoEntity("1", "Ethereum", "ETH", "eth", "crypto")
        )
        val expected = entities.map { it.toDomain() }
        whenever(repo.getAllCurrency()).thenReturn(flowOf(entities))

        val result = useCase().first()

        assertEquals(expected, result)
    }

    @Test
    fun `emits error when repo fails to get purchased`() = runTest {
        val errorMessage = "Failed to fetch"
        whenever(repo.getAllCurrency()).thenReturn(flow { throw RuntimeException(errorMessage) })

        val flow = useCase()

        val exception = assertFailsWith<RuntimeException> {
            flow.collect{ }
        }

        assertEquals(errorMessage, exception.message)
    }
}