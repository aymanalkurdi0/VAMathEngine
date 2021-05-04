package com.va.task

import androidx.work.Data
import androidx.work.WorkInfo
import java.util.*

data class JobStateBean(
    val mId: UUID, val mState: WorkInfo.State, val mOutputData: Data, val mTags: Set<String>,
    var mProgress: Data
)