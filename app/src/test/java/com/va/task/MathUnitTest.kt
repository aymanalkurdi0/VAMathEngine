package com.va.task

import org.junit.Assert
import org.junit.Test

class MathUnitTest {
    @Test
    fun addition() {

        val math = MathQuestion(
            intArrayOf(1, 2, 3), Operator.ADDITION, 500
        )

        Assert.assertEquals("(1, 2, 3), Operator.ADDITION", 6.0, math.execute(), 0.001)
    }

    @Test
    fun division() {

        val math = MathQuestion(
            intArrayOf(1, 2, 3), Operator.DIVISION, 500
        )

        Assert.assertEquals("(1, 2, 3), Operator.DIVISION", 0.166, math.execute(), 0.001)
    }

    @Test
    fun multiplication() {

        val math = MathQuestion(
            intArrayOf(1, 2, 3), Operator.MULTIPLICATION, 500
        )

        Assert.assertEquals(
            "(1, 2, 3), Operator.MULTIPLICATION",
            6.0,
            math.execute(),
            0.001
        )
    }

    @Test
    fun subtraction() {

        val math = MathQuestion(
            intArrayOf(1, 2, 3), Operator.SUBTRACTION, 500
        )

        Assert.assertEquals(
            "(1, 2, 3), Operator.SUBTRACTION",
            -4.0,
            math.execute(),
            0.001
        )
    }

    @Test(expected = ArithmeticException::class)
    fun divisionWithZero() {

        val math = MathQuestion(
            intArrayOf(1, 2, 3, 0), Operator.DIVISION, 500
        )

        math.execute()


    }

}