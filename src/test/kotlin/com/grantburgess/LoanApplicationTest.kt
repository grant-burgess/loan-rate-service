package com.grantburgess

import com.grantburgess.domain.InMemoryLenderRepository
import com.grantburgess.domain.Lender
import com.grantburgess.domain.LoanApplication
import com.grantburgess.fixtures.LenderRepositoryDummy
import com.grantburgess.fixtures.LenderRepositoryFake
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoanApplicationTest {
    @Rule
    @JvmField
    val expectedException = ExpectedException.none()
    private val lenderRepositoryDummy = LenderRepositoryDummy()
    private val lenderRepositoryFake = LenderRepositoryFake()
    private val inMemoryLenderRepository = InMemoryLenderRepository()

    @Before
    fun setup() {
        inMemoryLenderRepository.save(Lender("Jane",0.069f,480f))
        inMemoryLenderRepository.save(Lender("Fred",0.071f,520f))
        inMemoryLenderRepository.save(Lender("Angela",0.071f,60f))
        inMemoryLenderRepository.save(Lender("Dave",0.074f,140f))
        inMemoryLenderRepository.save(Lender("Bob",0.075f,640f))
        inMemoryLenderRepository.save(Lender("John",0.081f,320f))
        inMemoryLenderRepository.save(Lender("Mary",0.104f,170f))
    }

    @Test
    fun `given a loan application with a loan amount less than 1000 should fail`() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Loan amount needs to be between £1000 and £15000 inclusive.")

        LoanApplication(100, lenderRepositoryDummy)
    }

    @Test fun `given a loan application with a loan amount greater than 15000 should fail`() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Loan amount needs to be between £1000 and £15000 inclusive.")

        LoanApplication(16000, lenderRepositoryDummy)
    }

    @Test fun `given a loan application of 1000 with no sufficient offers from lenders should produce no quote`() {
        val loanApplication = LoanApplication(1000, lenderRepositoryDummy)
        val quote = loanApplication.quote()

        assertNull(quote)
    }

    @Test fun `given a loan application of 1000 and one lender should produce quote`() {
        val loanApplication = LoanApplication(1000, lenderRepositoryFake)
        val quote = loanApplication.quote()

        assertEquals(quote?.requestedAmount, 1000)
        assertEquals(quote?.rate, 0.07f)
        assertEquals(quote?.monthlyRepayment, 29.78987f)
        assertEquals(quote?.totalRepayment, 1072.4353f)
    }

    @Test fun `given a loan application of 1000 and eight lenders should produce quote`() {
        // GIVEN
        val loanApplication = LoanApplication(1000, inMemoryLenderRepository)

        // WHEN
        val quote = loanApplication.quote()

        // THEN
        assertEquals(quote?.requestedAmount, 1000)
        assertEquals(quote?.rate, 0.071f)
        assertEquals(quote?.monthlyRepayment, 29.819614f)
        assertEquals(quote?.totalRepayment, 1073.5061f)
    }

    @Test fun `given a loan application of 2100 and eight lenders should produce quote`() {
        // GIVEN
        val loanApplication = LoanApplication(2100, inMemoryLenderRepository)

        // WHEN
        val quote = loanApplication.quote()

        // THEN
        assertEquals(quote?.requestedAmount, 2100)
        assertEquals(quote?.rate, 0.081f)
        assertEquals(quote?.monthlyRepayment, 63.249214f)
        assertEquals(quote?.totalRepayment, 2276.9717f)
    }



    @Test fun `given a loan application of 3000 and eight lenders should produce no quote`() {
        // GIVEN
        val loanApplication = LoanApplication(3000, inMemoryLenderRepository)

        // WHEN
        val quote = loanApplication.quote()

        // THEN
        assertNull(quote)
    }
}