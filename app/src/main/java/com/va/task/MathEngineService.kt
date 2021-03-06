/*
 * Created by Ayman Alkurdi on 5/5/21 3:58 AM
 * Copyright (c) 2021 . All rights reserved.
 */

package com.va.task

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import kotlin.random.Random
// TODO: 5/5/2021 Documentation
class MathEngineService : JobIntentService() {


    override fun onHandleWork(intent: Intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        onHandleIntent(intent)

    }

    // remove override and make onHandleIntent private.
    private fun onHandleIntent(intent: Intent?) {
        val bean = intent?.getSerializableExtra(Constants.kMathQuestion) as? MathQuestion
            ?: throw RuntimeException("Arguments can't be null")

        createAJob(bean)


    }

    private fun createAJob(data: MathQuestion) {

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        val request = OneTimeWorkRequest.Builder(CalWorker::class.java)
            .setInitialDelay(data.delayTime, TimeUnit.SECONDS)
            .addTag(Constants.kJobsTag)
            .setInputData(myData)
            .build()

        val workManager = WorkManager.getInstance(this)

        workManager.enqueue(request)

    }



    // convenient method for starting the service.
    companion object {
        private const val jobId = 1
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, MathEngineService::class.java, jobId, intent)
        }
    }

}