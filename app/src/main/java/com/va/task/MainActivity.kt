package com.va.task

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.va.task.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var input: String?=null
    private lateinit var mBinding: ActivityMainBinding

    @Inject
    lateinit var activeJobsAdapter: ActiveJobsAdapter

    @Inject
    lateinit var completedJobsAdapter: ActiveJobsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.etNumbers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                input = s?.replace(Regex("\\s+|,+"), "*")?.trimStart { c ->c.equals("\\s+|,+")}

                mBinding.tvFun.text = input.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }


        }

        )
        mBinding.rvActiveJobs.adapter = activeJobsAdapter

        mBinding.rvCompletedJobs.adapter = completedJobsAdapter

        val math = MathQuestion(
            intArrayOf(1, 2, 3), Operator.ADDITION, 15
        )

        createAJob(math)

    }

    override fun onStart() {
        super.onStart()
        //Start Listening on works
        startListener()
    }


    private fun createAJob(data: MathQuestion) {

        val intent = Intent(this, MathEngineService::class.java)

        intent.putExtra(Constants.kMathQuestion, data)

        MathEngineService.enqueueWork(this, intent)
    }

    private fun startListener() {
        WorkManager.getInstance(this)
            .getWorkInfosLiveData(WorkQuery.Builder.fromTags(listOf(Constants.kJobsTag)).build())
            .observe(this) {

                it.sortedBy { workInfo -> workInfo.id }.apply {

                    activeJobsAdapter.data.clear()
                    activeJobsAdapter.data.addAll((this.filter { workInfo ->
                        workInfo.state.ordinal == 0 || workInfo.state.ordinal == 1
                    }))
                    activeJobsAdapter.notifyDataSetChanged()

                    completedJobsAdapter.data.clear()
                    completedJobsAdapter.data.addAll((this.filter { workInfo ->
                        workInfo.state.ordinal == 2
                    }))
                    completedJobsAdapter.notifyDataSetChanged()
                }

            }
    }


    override fun onStop() {

        WorkManager.getInstance(this)
            .getWorkInfosLiveData(WorkQuery.Builder.fromTags(listOf(Constants.kJobsTag)).build())
            .removeObservers(this)

        //Uncommented this line if u wanna cancel uncompleted works
//        WorkManager.getInstance(this).cancelAllWorkByTag(Constants.kJobsTag);

        super.onStop()
    }


}