package com.va.task

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.va.task.databinding.ItemActiveJobBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ActiveJobsAdapter @Inject constructor(@ActivityContext private val context: Context) :
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
        itemBinding: ItemActiveJobBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener { view ->
                adapterInterface?.onItemClick(
                    view,
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
        }

        fun bind(bean: WorkInfo) {

        }
    }

    interface AdapterInterface {
        fun onItemClick(view: View, bean: WorkInfo, position: Int)
    }
}
