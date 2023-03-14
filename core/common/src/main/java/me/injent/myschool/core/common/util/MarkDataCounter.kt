package me.injent.myschool.core.common.util

class MarkDataCounter(
    private val minMark: Float,
    private val maxMark: Float
) {
    fun successPercentageInClass(value: Float): Int {
        val percentage = ((value - minMark) / (maxMark - minMark) * 100).toInt()
        return percentage.coerceIn(0, 100)
    }
}