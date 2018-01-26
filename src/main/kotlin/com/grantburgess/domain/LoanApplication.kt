package com.grantburgess.domain

import com.grantburgess.takeAndAccumulateUntil

class LoanApplication(private val loanAmount: Long, private val lenderRepository: LenderRepository) {
    init {
        when (loanAmount) {
            !in 1000..15000 -> throw IllegalArgumentException("Loan amount needs to be between £1000 and £15000 inclusive.")
        }
    }

    // I made a conscious decision to use a nullable type
    // Though I was considering something similar to the Scala Either<left, right> class
    // subclassing also came to mind e.g. empty quote and so forth
    fun quote(): Quote? {
        // scoped locally, only used once
        fun calculateTotalRepayment(lenderRate: Float): Float {
            // Formula to calculate monthly compounding interest
            // FV = Future Value
            // PV = Present Value
            // r  = interest rate
            // n  = number of periods
            // FV = PV × (1+r/n)n
            val numberOfPeriods = 36.0
            val interestRate = 1 + lenderRate / numberOfPeriods

            return (loanAmount * Math.pow(interestRate, numberOfPeriods)).toFloat()
        }

        val loanOffer = findLoanOffer()

        return loanOffer?.let {
            val totalRepayment = calculateTotalRepayment(it.rate)
            Quote(loanAmount, it.rate, totalRepayment / 36, totalRepayment)
        }
    }

    private fun findLoanOffer(): LoanOffer? {
        val lenders = lenderRepository.fetchLendersSortedByRateAscAmountAvailableDesc()

        // scoped locally, only used once
        fun findLoanOfferInternal(): LoanOffer? {
            val (conditionMet, availableLenders) = lenders.takeAndAccumulateUntil(loanAmount) { it.available }

            return if (conditionMet) {
                val rate = availableLenders.map { it.rate }.max() ?: 0f
                LoanOffer(rate, availableLenders)
            } else null
        }

        return if (lenders.isEmpty()) {
            null
        } else {
            if (lenders.size == 1) {
                // one lender with the amount available
                if (lenders.single().available >= loanAmount)
                    LoanOffer(lenders.single().rate, lenders)
                else null
            } else findLoanOfferInternal()
        }
    }

    data class LoanOffer(val rate: Float, val lenders: List<Lender>)

    data class Quote(
        val requestedAmount: Long,
        val rate: Float,
        val monthlyRepayment: Float,
        val totalRepayment: Float
    )
}