package com.va.task

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.google.android.material.tabs.TabLayout
import com.va.task.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var inputs: ArrayList<Int> = arrayListOf()
    private var operation = Operator.ADDITION
    private var operationSymbols = " + "
    private lateinit var mBinding: ActivityMainBinding

    @Inject
    lateinit var activeJobsAdapter: ActiveJobsAdapter

    @Inject
    lateinit var completedJobsAdapter: ActiveJobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initialListeners()

        setupRvAdapters()

    }

    override fun onStart() {
        super.onStart()
        //Start Listening on works
        startListener()
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

    private fun setupRvAdapters() {
        mBinding.rvActiveJobs.adapter = activeJobsAdapter
        mBinding.rvCompletedJobs.adapter = completedJobsAdapter
    }

    private fun initialListeners() {

        mBinding.tlOperation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        operation = Operator.ADDITION
                        operationSymbols = " + "
                    }
                    1 -> {
                        operation = Operator.SUBTRACTION
                        operationSymbols = " - "
                    }
                    2 -> {
                        operation = Operator.MULTIPLICATION
                        operationSymbols = " × "
                    }
                    3 -> {
                        operation = Operator.DIVISION
                        operationSymbols = " ÷ "

                    }
                }
                updateFun()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        mBinding.btnAdd.setOnClickListener {
            inputs.add(mBinding.etNumbers.text.toString().toInt())
            mBinding.etNumbers.text.clear()
            updateFun()
        }

        mBinding.btnClear.setOnClickListener {
            clearInputs()
        }

        mBinding.btnCalculate.setOnClickListener {

            if (inputs.size < 2) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_input_should_be_more_then),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            var delay: Long = 0

            if (mBinding.etDelay.text.isNotEmpty())
                delay = mBinding.etDelay.text.toString().toLong()

            val math = MathQuestion(
                inputs.toIntArray(), operation, delay
            )

            createAJob(math)

//            clearInputs()
        }
    }

    private fun clearInputs() {
        mBinding.etNumbers.text.clear()
        mBinding.tvFun.text = ""
        inputs.clear()
    }

    private fun updateFun() {
        val string = StringBuilder()

        inputs.forEachIndexed { index, s ->
            if (index != 0)
                string.append(operationSymbols)
            string.append(s)

        }
        mBinding.tvFun.text = string

    }

    private fun createAJob(data: MathQuestion) {

        val intent = Intent(this, MathEngineService::class.java)

        intent.putExtra(Constants.kMathQuestion, data)

        MathEngineService.enqueueWork(this, intent)
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