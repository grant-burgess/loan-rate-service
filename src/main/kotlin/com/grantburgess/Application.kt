package com.grantburgess

import com.grantburgess.domain.InMemoryLenderRepository
import com.grantburgess.domain.Lender
import com.grantburgess.domain.LoanApplication
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    validateCommandLineInput(args)

    // populate the lender repository
    // this is a one time operation
    // typically we would use a database or service call to retrieve lender information
    val lenderRepository = loadLenderRepositoryWithData(File(args[0]))
    val loanAmount = args[1].toLong()
    val loanApplication = LoanApplication(loanAmount, lenderRepository)

    loanApplication.quote()
        ?.let {
            // print out the result if a quote was available
            print(
                """
                    |Requested amount: £${it.requestedAmount}
                    |Rate: $${"%.1f".format(it.rate * 100)}%
                    |Monthly repayment: £${"%.2f".format(it.monthlyRepayment)}
                    |Total repayment: £${"%.2f".format(it.totalRepayment)}
                    """.trimMargin()
            )
        }
        ?: print("Sorry, we are not able to provide a quote at this time.")

}

fun loadLenderRepositoryWithData(file: File): InMemoryLenderRepository {
    val inMemoryLenderRepository = InMemoryLenderRepository()
    file
        .readLines()
        .drop(1) // ignore the header row
        .map { Lender.parse(it) }
        .forEach { inMemoryLenderRepository.save(it) }

    return inMemoryLenderRepository
}

private fun validateCommandLineInput(args: Array<String>) {
    if (args.size != 2) {
        println("ERROR. Expected 2 arguments instead received ${args.size}.")
        exitProcess(0)
    }
    if (Files.notExists(Paths.get(args[0]))) {
        println("ERROR. File '${args[0]}' does not exist.")
        exitProcess(0)
    }
    if (args[1].toIntOrNull() == null) {
        println("ERROR. Expected a loan amount instead received '${args[1]}'.")
        exitProcess(0)
    }
}