package me.injent.myschool.core.common.util

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

/**
 * Сlass used for operations with marks
 */
class MarkDataCounter(
    private val minMark: Float,
    private val maxMark: Float
) {
    /**
     * Calculates mark percentage between min and max mark values
     * Example:
     * [minMark] = 2
     * [maxMark] = 5
     * [value] = 3,5
     * Result will be 50
     *
     *
     * @param value: mark value that needs to be represented in percentage
     * @return a percentage value from 0 to 100
     */
    @IntRange(from = 0, to = 100)
    fun markInPercentage(value: Float): Int {
        val percentage = ((value - minMark) / (maxMark - minMark) * 100).toInt()
        return percentage.coerceIn(0, 100)
    }

    @FloatRange(from = 0.0, to = 1.0, fromInclusive = true, toInclusive = true)
    fun markInFloat(value: Float): Float {
        return markInPercentage(value) / 100f
    }
}