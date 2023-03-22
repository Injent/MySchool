package me.injent.myschool.core.data.repository

import kotlinx.datetime.toKotlinLocalDateTime
import me.injent.myschool.core.database.dao.MarkDao
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

interface StatisticRepository {
    suspend fun getAverageMarksByWeek(personId: Long): List<Float>
}

class StatisticsRepositoryImpl @Inject constructor(
    private val markDao: MarkDao
) : StatisticRepository {
    override suspend fun getAverageMarksByWeek(personId: Long): List<Float> {
        val currentDateTime = LocalDateTime.now()
        val startOfWeek = currentDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = currentDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val listOfWeek = mutableListOf<LocalDateTime>()
        var date = startOfWeek
        while (!date.isAfter(endOfWeek)) {
            listOfWeek.add(
                date.withHour(23).withMinute(59)
            )
            date = date.plusDays(1)
        }

        val averageMarks = mutableListOf<Float>()

        listOfWeek.forEach { beforeDate ->
            averageMarks.add(
                markDao.getPersonAverageMark(
                    personId = personId,
                    beforeDate = beforeDate.toKotlinLocalDateTime()
                )
            )
        }
        return averageMarks.toList()
    }
}