package me.injent.myschool.core.common.util

class MarkDataCounter(
    private val minMark: Float,
    private val maxMark: Float
) {
    fun markInPercentage(value: Float): Int {
        val percentage = ((value - minMark) / (maxMark - minMark) * 100).toInt()
        return percentage.coerceIn(0, 100)
    }

    fun markInFloat(value: Float): Float {
        return markInPercentage(value) / 100f
    }
}