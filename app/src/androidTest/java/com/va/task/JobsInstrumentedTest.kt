package com.va.task

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestWorkerBuilder
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class JobsInstrumentedTest {
    private lateinit var context: Context
    private lateinit var executor: Executor

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
    }

    @Test
    fun testHappyCaseJobsWorker() {

        val data = MathQuestion(
            intArrayOf(1, 2, 3), Operator.ADDITION, 500
        )

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        val worker = TestWorkerBuilder<CalWorker>(
            context = context,
            executor = executor,
            inputData = myData
        ).build()


        val result = worker.doWork()

        val output = Data.Builder()
            .putDouble(Constants.kResult, 6.0)
            .build()

        val expected = ListenableWorker.Result.success(output)

        assertThat(result, `is`(expected))
    }


    @Test
    fun testWorstCaseJobsWorker() {

        val data = MathQuestion(
            intArrayOf(1, 2, 3,0), Operator.DIVISION, 500
        )

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        val worker = TestWorkerBuilder<CalWorker>(
            context = context,
            executor = executor,
            inputData = myData
        ).build()


        val result = worker.doWork()

        val output = Data.Builder()
            .putString(Constants.kResult,context.resources.getString(R.string.error_undefined) )
            .build()

        val expected = ListenableWorker.Result.Failure(output)

        assertThat(result, `is`(expected))
    }
}