/*
 * Created by Ayman Alkurdi on 5/5/21 3:58 AM
 * Copyright (c) 2021 . All rights reserved.
 */

package com.va.task

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ServiceTestRule
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
// TODO: 5/5/2021 Documentation and reformat methods names
@RunWith(AndroidJUnit4::class)
class MathEngineServiceTest {

    private lateinit var context: Context

    @get:Rule
    val serviceRule = ServiceTestRule()

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
    fun testMathEngineService() {

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            MathEngineService::class.java
        ).apply {
            val bean = MathQuestion(
                intArrayOf(1, 2, 3), Operator.ADDITION, 0
            )
            putExtra(Constants.kMathQuestion, bean)
        }
        MathEngineService.enqueueWork(ApplicationProvider.getApplicationContext(), intent)

        Thread.sleep(TimeUnit.SECONDS.toMillis(2))

        val workManager = WorkManager.getInstance(context)

        val workInfo = workManager.getWorkInfosByTag(Constants.kJobsTag).get()[0]

        val outputData = workInfo.outputData

        val output = Data.Builder()
            .putDouble(Constants.kResult, 6.0)
            .build()

        ViewMatchers.assertThat(workInfo.state, CoreMatchers.`is`(WorkInfo.State.SUCCEEDED))
        ViewMatchers.assertThat(outputData, CoreMatchers.`is`(output))

    }
}