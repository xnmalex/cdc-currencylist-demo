package com.cdc.currencylistdemo

import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity
import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.domain.model.toDomain
import com.cdc.currencylistdemo.domain.usecase.GetCurrenciesByTypeUseCase
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

class GetCurrenciesByTypeUseCaseTest {
    private val repo = mock<CurrencyLocalRepository>()
    private lateinit var useCase: GetCurrenciesByTypeUseCase

    @Before
    fun setup() {
        useCase = GetCurrenciesByTypeUseCase(repo)
    }

    @Test
    fun `returns currencies by type`() = runTest {
        val entities = listOf(
            CurrencyInfoEntity("1", "Bitcoin", "BTC", "btc", "crypto")
        )
        val expected = entities.map { it.toDomain() }

        whenever(repo.getByType("crypto")).thenReturn(flowOf(entities))

        val result = useCase("crypto").first()

        assertEquals(expected, result)
    }

    @Test
    fun `emits error when repo fails to get by type`() = runTest {
        val errorMessage = "Type load failed"
        whenever(repo.getByType("crypto")).thenReturn(flow { throw RuntimeException(errorMessage) })

        val flow = useCase("crypto")

        val exception = assertFailsWith<RuntimeException> {
            flow.collect{}
        }

        assertEquals(errorMessage, exception.message)
    }
}