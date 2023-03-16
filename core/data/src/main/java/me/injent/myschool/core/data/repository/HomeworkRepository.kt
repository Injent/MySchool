package me.injent.myschool.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.*
import me.injent.myschool.core.common.util.currentDateTimeAtEndOfDay
import me.injent.myschool.core.common.util.currentDateTimeAtStartOfDay
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.Homework
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModelList
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

interface HomeworkRepository : Syncable {
    val homeworkToday: Flow<List<Homework>>
}

@RepoDependency(UserContextRepository::class)
class OnlineHomeworkRepository @Inject constructor(
    private val userContextRepository: UserContextRepository,
    private val networkDataSource: DnevnikNetworkDataSource
) : HomeworkRepository {
    override suspend fun synchronize(): Boolean = try {

        true
    } catch (e: Exception) {
        Log.e("SubjectRepository", "Failed to sync")
        e.printStackTrace()
        false
    }

    override val homeworkToday: Flow<List<Homework>>
        get() = flow {
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
        }
}