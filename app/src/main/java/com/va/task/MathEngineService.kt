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

class MathEngineService : JobIntentService() {


    // This method is called when service starts instead of onHandleIntent
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


    override fun onDestroy() {
        super.onDestroy()
    }

    // convenient method for starting the service.
    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, MathEngineService::class.java, Random.nextInt(), intent)
        }
    }

}