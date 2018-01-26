package com.grantburgess.domain

interface LenderRepository {
    fun save(lender: Lender)
    // the function name is wordy, but you know what it's going to give you
    fun fetchLendersSortedByRateAscAmountAvailableDesc(): List<Lender>
}

class InMemoryLenderRepository : LenderRepository {
    private val lenders: MutableSet<Lender> = HashSet()

    override fun save(lender: Lender) {
        lenders.add(lender)
    }

    override fun fetchLendersSortedByRateAscAmountAvailableDesc(): List<Lender> {
        return lenders
            .sortedWith(
                compareBy<Lender> { it.rate }
                .thenByDescending { it.available }
            )
    }
}

data class Lender(val name: String, val rate: Float, val available: Float) {
    companion object {
        fun parse(value: String): Lender {
            val items = value
                .split(",")
                .filter(String::isNotBlank)
            when {
                items.size != 3 -> throw IndexOutOfBoundsException("Expected 3 values received ${items.size}.")
                items[1].toFloatOrNull() == null -> throw IllegalArgumentException("Expected a decimal point number received '${items[1]}'")
                items[2].toFloatOrNull() == null -> throw IllegalArgumentException("Expected a decimal point number received '${items[2]}'")
            }

            return Lender(items[0].trim(), items[1].toFloat(), items[2].toFloat())
        }
    }
}