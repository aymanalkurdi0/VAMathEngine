/*
 * Created by Ayman Alkurdi on 5/5/21 3:58 AM
 * Copyright (c) 2021 . All rights reserved.
 */

package com.va.task

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class CalWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        // Get data
        val list = inputData.getIntArray(Constants.kList)
        val operator = inputData.getString(Constants.kOperator);

        // Check if null or empty
        if (list == null || list.isEmpty() || operator == null) {
            return Result.failure()
        }

        // Create MathQuestion object for executing action
        val mathQuestion = MathQuestion(list, Operator.valueOf(operator))

        try {

            // Execute action
            val result = mathQuestion.execute()

            // Create data object for put the result
            val output = Data.Builder()
                .putDouble(Constants.kResult, result)
                .build()

            // Return success with output
            return Result.success(output)

        } catch (e: ArithmeticException) {

            var message = ""

            // Check exception message if null
            e.message?.let { message = it } ?: kotlin.run {
                message = appContext.resources.getString(R.string.error_undefined)
            }

            // Create data object for put the result
            val output = Data.Builder()
                .putString(Constants.kResult, message)
                .build()

            // Return failure
            return Result.failure(output)

        }
    }
}