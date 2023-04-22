package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.toKotlinLocalDateTime
import me.injent.myschool.core.database.dao.MarkDao
import me.injent.myschool.core.database.dao.PersonDao
import me.injent.myschool.core.database.dao.SubjectDao
import me.injent.myschool.core.database.model.asExternalModel
import me.injent.myschool.core.model.Period
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Subject
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

interface StatisticRepository {
    suspend fun getAverageMarksByWeek(personId: Long): List<Float>
    fun getBestStudentsBySubject(period: Period): Flow<Map<Subject, Person>>
}

class StatisticsRepositoryImpl @Inject constructor(
    private val markDao: MarkDao,
    private val subjectDao: SubjectDao,
    private val personDao: PersonDao
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

    override fun getBestStudentsBySubject(period: Period): Flow<Map<Subject, Person>> = flow {
        val bestStudentsBySubject: MutableMap<Subject, Person> = mutableMapOf()
        for (subject in subjectDao.getSubjects().first()) {
            val person = personDao.getPerson(
                markDao.getBestPersonsIdBySubject(
                    subject.id, period.dateStart, period.dateFinish
                ).firstOrNull() ?: 0
            )?.asExternalModel()

            if (person != null) {
                bestStudentsBySubject[subject.asExternalModel()] = person
                emit(bestStudentsBySubject)
            }
        }
    }
}