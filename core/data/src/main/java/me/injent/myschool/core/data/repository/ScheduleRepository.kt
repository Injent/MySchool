package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.*
import kotlinx.datetime.*
import me.injent.myschool.core.common.util.epochSeconds
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.model.Homework
import me.injent.myschool.core.model.Schedule
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.NetworkAttachment
import me.injent.myschool.core.network.model.asExternalModel
import me.injent.myschool.core.network.model.asExternalModelList
import javax.inject.Inject

interface ScheduleRepository {
    suspend fun getHomework(startDate: LocalDateTime, endDate: LocalDateTime): List<Homework>
    fun getSchedule(date: LocalDate): Flow<Schedule?>
}

@RepoDependency(UserContextRepository::class)
class RemoteScheduleRepository @Inject constructor(
    private val userContextRepository: UserContextRepository,
    private val networkDataSource: DnevnikNetworkDataSource
) : ScheduleRepository {

    override suspend fun getHomework(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Homework> {
        val schoolId = userContextRepository.userContext.first()!!.school.id

        return networkDataSource.getHomeworks(
            schoolId = schoolId,
            from = startDate,
            to = endDate
        ).asExternalModelList()
    }

    override fun getSchedule(
        date: LocalDate
    ): Flow<Schedule?> = flow {
        val userContext = userContextRepository.userContext.first()!!
        val schedule = networkDataSource.getPersonSchedule(
            personId = userContext.personId,
            schoolId = userContext.school.id,
            groupId = userContext.group.id,
            startDate = date.atTime(0, 0).epochSeconds,
            finishDate = date.atTime(23, 59, 59).epochSeconds
        )
        if (schedule.days.isEmpty()) {
            emit(null)
            return@flow
        }

        emit(schedule.asExternalModel())

        val lessonsWithFiles = schedule.days.first().lessons
            .map { lesson ->
                if (!lesson.homework?.text.isNullOrEmpty()) {
                    var teacherName = ""
                    val attachments = networkDataSource.getLesson(lesson.id).works
                        .filter { it.type == "Homework" }
                        .flatMap { work ->
                            val data = networkDataSource.getHomeworkData(work.id)
                            teacherName = data.teachers.first().shortName
                            data.files
                        }

                    lesson.asExternalModel().copy(
                        attachments = attachments.map(NetworkAttachment::asExternalModel),
                        teacherName = teacherName
                    )
                } else {
                    lesson.asExternalModel()
                }
            }

        emit(schedule.asExternalModel().copy(lessonsWithFiles))
    }
        .cancellable()
}