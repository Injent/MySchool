package me.injent.myschool.updates.monitor

import androidx.annotation.IntRange

data class UpdateDownloadData(
    val status: Int,
    @IntRange(from = 0, to = 100) val progress: Int
)
