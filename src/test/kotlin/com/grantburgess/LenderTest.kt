package com.grantburgess

import com.grantburgess.domain.Lender
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.assertEquals

class LenderTest {
    @Rule
    @JvmField
    val expectedException = ExpectedException.none()

    @Test fun `given a well formed string should parse`() {
        assertEquals(Lender("Bob", 0.07f, 5500f), Lender.parse("Bob,0.07,5500f"))
        assertEquals(Lender("Bob", 0.07f, 5500f), Lender.parse("Bob, 0.07,  5500f"))
        assertEquals(Lender("Bob", 0.07f, 5500f), Lender.parse(" Bob , 0.07,  5500f"))
    }

    @Test fun `given an empty string should fail`() {
        expectedException.expect(IndexOutOfBoundsException::class.java)
        expectedException.expectMessage("Expected 3 values received 0.")

        Lender.parse("")
    }

    @Test fun `given a string with no values should fail`() {
        expectedException.expect(IndexOutOfBoundsException::class.java)
        expectedException.expectMessage("Expected 3 values received 0.")

        Lender.parse(",,")
    }

    @Test fun `given a string with too few comma separated parameters should fail`() {
        expectedException.expect(IndexOutOfBoundsException::class.java)
        expectedException.expectMessage("Expected 3 values received 2.")

        Lender.parse("Bob,0.07")
    }

    @Test fun `given a string with an invalid rate parameters should fail`() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Expected a decimal point number received 'Alice'")

        Lender.parse("Bob,Alice,5500")
    }

    @Test fun `given a string with an available amount parameters should fail`() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Expected a decimal point number received 'Alice'")

        Lender.parse("Bob,0.07,Alice")
    }
}