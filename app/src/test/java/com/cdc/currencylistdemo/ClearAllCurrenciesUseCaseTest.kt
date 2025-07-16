package com.cdc.currencylistdemo

import com.cdc.currencylistdemo.data.local.repository.CurrencyLocalRepository
import com.cdc.currencylistdemo.domain.usecase.ClearAllCurrenciesUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertFailsWith

class ClearAllCurrenciesUseCaseTest {
    private val repo = mock<CurrencyLocalRepository>()
    private lateinit var useCase: ClearAllCurrenciesUseCase

    @Before
    fun setup() {
        useCase = ClearAllCurrenciesUseCase(repo)
    }

    @Test
    fun `invokes clear all from repository`() = runTest {
        useCase()

        verify(repo).clearAll()
    }

    @Test
    fun `throws exception when repo fails to clear all`() = runTest {
        val errorMessage = "DB failure"
        whenever(repo.clearAll()).thenThrow(RuntimeException(errorMessage))

        val exception = assertFailsWith<RuntimeException> {
            useCase()
        }

        assertEquals(errorMessage, exception.message)
    }
}