package com.va.task

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService

class CalculatorService : JobIntentService() {


    // This method is called when service starts instead of onHandleIntent
    override fun onHandleWork(intent: Intent) {
        onHandleIntent(intent)

    }

    // remove override and make onHandleIntent private.
    private fun onHandleIntent(intent: Intent?) {
        val dataString = intent?.dataString

    }

    // convenient method for starting the service.
    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, CalculatorService::class.java, 1, intent)
        }
    }

}