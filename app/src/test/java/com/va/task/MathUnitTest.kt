/*
 * Created by Ayman Alkurdi on 5/5/21 3:58 AM
 * Copyright (c) 2021 . All rights reserved.
 */

package com.va.task

import org.junit.Assert
import org.junit.Test
// TODO: 5/5/2021 Documentation and reformat methods names
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