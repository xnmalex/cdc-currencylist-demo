package com.cdc.currencylistdemo

import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.domain.usecase.SearchCurrenciesUseCase
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

class SearchCurrenciesUseCaseTest {
    private val repo = mock<CurrencyLocalRepository>()
    private lateinit var useCase: SearchCurrenciesUseCase

    @Before
    fun setup() {
        useCase = SearchCurrenciesUseCase(repo)
    }

    @Test
    fun `filters and maps currencies by query`() = runTest {
        val entities = listOf(
            CurrencyInfoEntity("1", "Ethereum", "ETH", "eth", "crypto"),
            CurrencyInfoEntity("2", "Bitcoin", "BTC", "btc", "crypto")
        )

        whenever(repo.getByType("crypto")).thenReturn(flowOf(entities))

        val result = useCase("eth", "crypto").first()

        assertEquals(1, result.size)
        assertEquals("Ethereum", result[0].name)
    }

    @Test
    fun `filters and maps currencies by criteria foobar barfoo`() = runTest {
        val entities = listOf(
            CurrencyInfoEntity("1", "FooBar", "ETH", "eth", "crypto"),
            CurrencyInfoEntity("2", "Barfoo", "BTC", "btc", "crypto"),
        )

        whenever(repo.getByType("crypto")).thenReturn(flowOf(entities))

        val result = useCase("foo", "crypto").first()

        assertEquals(1, result.size)
        assertEquals("FooBar", result[0].name)
    }

    @Test
    fun `filters and maps currencies by criteria partial match with space`() = runTest {
        val entities = listOf(
            CurrencyInfoEntity("1", "Ethereum Classic", "ETC", "eth", "crypto"),
            CurrencyInfoEntity("2", "Tronclassic", "TC", "tc", "crypto"),
        )

        whenever(repo.getByType("crypto")).thenReturn(flowOf(entities))

        val result = useCase("Classic", "crypto").first()

        assertEquals(1, result.size)
        assertEquals("Ethereum Classic", result[0].name)
    }

    @Test
    fun `filters and maps currencies by criteria symbol search term`() = runTest {
        val entities = listOf(
            CurrencyInfoEntity("1", "Ethereum Classic", "ETC", "etc", "crypto"),
            CurrencyInfoEntity("2", "Ethereum", "ETH", "eth", "crypto"),
            CurrencyInfoEntity("2", "Ethereum Next", "ETN", "etn", "crypto"),
            CurrencyInfoEntity("3", "Bet Coin", "BET", "bet", "crypto"),
        )

        whenever(repo.getByType("crypto")).thenReturn(flowOf(entities))

        val result = useCase("ET", "crypto").first()

        assertEquals(3, result.size)
        assertEquals("Ethereum Classic", result[0].name)
    }


    @Test
    fun `emits error when searching currencies fails`() = runTest {
        val errorMessage = "DB error during search"
        whenever(repo.getByType("crypto")).thenReturn(flow { throw RuntimeException(errorMessage) })

        val flow = useCase("eth", "crypto")

        val exception = assertFailsWith<RuntimeException> {
            flow.collect {}
        }

        assertEquals(errorMessage, exception.message)
    }
}