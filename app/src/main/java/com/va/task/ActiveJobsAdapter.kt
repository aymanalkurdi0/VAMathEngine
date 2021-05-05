/*
 * Created by Ayman Alkurdi on 5/5/21 3:57 AM
 * Copyright (c) 2021 . All rights reserved.
 */

package com.va.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.va.task.databinding.ItemActiveJobBinding
import javax.inject.Inject

// TODO: 5/5/2021 Documentation
class ActiveJobsAdapter @Inject constructor() :
    RecyclerView.Adapter<ActiveJobsAdapter.ViewHolder>() {

    var adapterInterface: AdapterInterface? = null

    var data = ArrayList<WorkInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemActiveJobBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
    fun getItem(position: Int): WorkInfo = data[position]


    inner class ViewHolder(
        private val mBinding: ItemActiveJobBinding
    ) :
        RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.root.setOnClickListener { view ->
                adapterInterface?.onItemClick(
                    view,
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
        }

        fun bind(bean: WorkInfo) {
            mBinding.tvId.text = bean.id.toString()
            mBinding.tvState.text = bean.state.name
            if (bean.state.ordinal == 2) {
                mBinding.llState.visibility = View.VISIBLE
                mBinding.tvResult.text =
                    bean.outputData.getDouble(Constants.kResult, 0.0).toString()
            } else {
                mBinding.llState.visibility = View.GONE
            }

        }
    }

    interface AdapterInterface {
        fun onItemClick(view: View, bean: WorkInfo, position: Int)
    }
}
