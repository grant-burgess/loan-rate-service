package com.grantburgess.fixtures

import com.grantburgess.domain.Lender
import com.grantburgess.domain.LenderRepository

class LenderRepositoryDummy : LenderRepository {
    override fun save(lender: Lender) {
        TODO("not implemented")
    }

    override fun fetchLendersSortedByRateAscAmountAvailableDesc(): List<Lender> {
        return emptyList()
    }
}

class LenderRepositoryFake : LenderRepository {
    private val lenders: List<Lender> = listOf(Lender("Bob", 0.07f, 1000f))

    override fun save(lender: Lender) {
        TODO("not implemented")
    }

    override fun fetchLendersSortedByRateAscAmountAvailableDesc(): List<Lender> {
        return lenders
    }
}