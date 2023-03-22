package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.*
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.model.Homework
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModelList
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

interface HomeworkRepository {
    val homeworkToday: Flow<List<Homework>>
}

@RepoDependency(UserContextRepository::class)
class RemoteHomeworkRepository @Inject constructor(
    private val userContextRepository: UserContextRepository,
    private val networkDataSource: DnevnikNetworkDataSource
) : HomeworkRepository {

    override val homeworkToday: Flow<List<Homework>>
        get() = flow {
            try {
                val schoolId = userContextRepository.userContext.first()!!.school.id

                val from = Clock.System.now().plus(1L.days)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    .atTime(0, 0, 0)

                val to = Clock.System.now().plus(1L.days)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    .atTime(23, 59, 59)

                val homeworks = networkDataSource.getHomeworks(
                    schoolId = schoolId,
                    from = from,
                    to = to
                )
                emit(homeworks.asExternalModelList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}