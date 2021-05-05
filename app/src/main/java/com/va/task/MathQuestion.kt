/*
 * Created by Ayman Alkurdi on 5/5/21 3:58 AM
 * Copyright (c) 2021 . All rights reserved.
 */

package com.va.task

import java.io.Serializable
import java.lang.ArithmeticException
// TODO: 5/5/2021 Documentation
class MathQuestion(var list: IntArray, var operator: Operator, var delayTime: Long =0):
    Serializable {

    @Throws(ArithmeticException::class)
    fun execute(): Double {
        when (operator) {

            Operator.ADDITION -> {
                var result = 0.0

                for ((index, value) in list.withIndex()) {
                    if (index == 0)
                        result = value.toDouble()
                    else
                        result += value
                }
                return result
            }

            Operator.SUBTRACTION -> {
                var result = 0.0
                for ((index, value) in list.withIndex()) {
                    if (index == 0)
                        result = value.toDouble()
                    else
                        result -= value
                }
                return result
            }

            Operator.MULTIPLICATION -> {

                var result = 0.0

                for ((index, value) in list.withIndex()) {
                    if (index == 0)
                        result = value.toDouble()
                    else
                        result *= value
                }
                return result
            }
            Operator.DIVISION -> {
                if(list.contains(0))
                    throw  ArithmeticException()
                var result = 0.0

                for ((index, value) in list.withIndex()) {
                    if (index == 0)
                        result = value.toDouble()
                    else
                        result /= value
                }
                return result
            }

        }
    }

}