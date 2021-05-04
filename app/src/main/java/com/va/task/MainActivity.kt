package com.va.task

import android.content.Intent
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
    private val workManager: WorkManager = WorkManager.getInstance(this)
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

        createAJob(math)
        startListener()

    }

    private fun createAJob(data: MathQuestion) {

        val intent = Intent(this, MathEngineService::class.java)
        intent.putExtra(Constants.kMathQuestion,data)

        MathEngineService.enqueueWork(this, intent)
    }

    private fun startListener() {
        workInfos = WorkManager.getInstance(this)
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