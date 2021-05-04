package com.va.task

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.work.*
import com.va.task.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var workInfos: LiveData<List<WorkInfo>>? = null
    private val workManager: WorkManager? = null
    private lateinit var mBinding: ActivityMainBinding

    @Inject
    lateinit var activeJobsAdapter: ActiveJobsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.rvActiveJobs.adapter = activeJobsAdapter


        val math = MathQuestion(
            intArrayOf(1, 2, 3), Operator.ADDITION, 500
        )

        createAJob(math);

    }

    private fun createAJob(data: MathQuestion) {

        val myConstraints = Constraints.Builder()
            .setTriggerContentMaxDelay(data.delayTime, TimeUnit.SECONDS)
            .build()

        val myData = Data.Builder()
            .putIntArray(Constants.kList, data.list)
            .putString(Constants.kOperator, data.operator.name)
            .build()

        val request = OneTimeWorkRequest.Builder(CalWorker::class.java)
            .addTag(Constants.kJobsTag)
            .setConstraints(myConstraints)
            .setInputData(myData)
            .build()


        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(request)

        startListener(workManager)
    }

    private fun startListener(workManager: WorkManager) {
        workInfos = workManager
            .getWorkInfosLiveData(WorkQuery.Builder.fromTags(listOf(Constants.kJobsTag)).build())
        workInfos?.observe(this) {
            Log.d(this.javaClass.name, "observed: ")
            if (it.isNullOrEmpty())
                activeJobsAdapter.data.clear()
            else
                activeJobsAdapter.data.addAll((it))
            activeJobsAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        workInfos?.removeObservers(this)
        workManager?.cancelAllWorkByTag(Constants.kJobsTag)
        super.onDestroy()
    }
}