/*
 * Created by Ayman Alkurdi on 5/5/21 3:58 AM
 * Copyright (c) 2021 . All rights reserved.
 */

package com.va.task

import android.content.Context
import android.util.Log
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

// TODO: 5/5/2021 Reformat methods names
@RunWith(AndroidJUnit4::class)
class WorkManagerInstrumentedTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    @Throws(Exception::class)
    fun testWorkManager() {
        // Define input data
        val data = MathQuestion(
            intArrayOf(1, 2, 3), Operator.ADDITION, 5000
        )

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        // Create request
        val request = OneTimeWorkRequest.Builder(CalWorker::class.java)
            .addTag(Constants.kJobsTag)
            .setInputData(myData)
            .build()

        val workManager = WorkManager.getInstance(context)

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        workManager.enqueue(request).result.get()

        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData

        // Create expected result
        val output = Data.Builder()
            .putDouble(Constants.kResult, 6.0)
            .build()

        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        assertThat(outputData, `is`(output))
    }

    @Test
    @Throws(Exception::class)
    fun testWorkManagerWithDelay() {
        // Define input data
        val data = MathQuestion(
            intArrayOf(1, 2, 3), Operator.ADDITION, 5
        )

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        // Create request
        val request = OneTimeWorkRequest.Builder(CalWorker::class.java)
            .addTag(Constants.kJobsTag)
            .setInitialDelay(data.delayTime, TimeUnit.SECONDS)
            .setInputData(myData)
            .build()

        val workManager = WorkManager.getInstance(context)

        // Get the TestDriver
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        // Enqueue
        workManager.enqueue(request).result.get()

        // Tells the WorkManager test framework that initial delays are now met.
        testDriver?.setInitialDelayMet(request.id)

        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData

        // Create expected result
        val output = Data.Builder()
            .putDouble(Constants.kResult, 6.0)
            .build()


        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        assertThat(outputData, `is`(output))

    }

    @Test
    @Throws(Exception::class)
    fun testWorkManagerWithDelayAndConstraints() {
        // Define input data
        val data = MathQuestion(
            intArrayOf(1, 2, 3), Operator.ADDITION, 5
        )

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        // Create constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create request
        val request = OneTimeWorkRequest.Builder(CalWorker::class.java)
            .addTag(Constants.kJobsTag)
            .setConstraints(constraints)
            .setInitialDelay(data.delayTime, TimeUnit.SECONDS)
            .setInputData(myData)
            .build()

        val workManager = WorkManager.getInstance(context)

        // Get the TestDriver
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        // Enqueue
        workManager.enqueue(request).result.get()

        // Tells the WorkManager test framework that initial delays are now met.
        testDriver?.setInitialDelayMet(request.id)

        // Tells the testing framework that all constraints are met.
        testDriver?.setAllConstraintsMet(request.id)

        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData

        // Create expected result
        val output = Data.Builder()
            .putDouble(Constants.kResult, 6.0)
            .build()


        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        assertThat(outputData, `is`(output))

    }

    @Test
    @Throws(Exception::class)
    fun testWorkManagerWorstCase() {
        // Define input data
        val data = MathQuestion(
            intArrayOf(1, 2, 3,0), Operator.DIVISION, 5000
        )

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        // Create request
        val request = OneTimeWorkRequest.Builder(CalWorker::class.java)
            .addTag(Constants.kJobsTag)
            .setInputData(myData)
            .build()

        val workManager = WorkManager.getInstance(context)

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        workManager.enqueue(request).result.get()

        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData

        // Create expected result
        val output = Data.Builder()
            .putString(Constants.kResult,context.resources.getString(R.string.error_undefined) )
            .build()


        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.FAILED))
        assertThat(outputData, `is`(output))

    }
}
