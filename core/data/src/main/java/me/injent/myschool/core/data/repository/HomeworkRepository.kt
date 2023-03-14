package me.injent.myschool.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.data.util.RepoDependency
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.Homework
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModelList
import javax.inject.Inject

@RepoDependency(UserDataRepository::class)
interface HomeworkRepository : Syncable {
    val homeworkToday: Flow<List<Homework>>
}

class OnlineHomeworkRepository @Inject constructor(
    private val userDataSource: MsPreferencesDataSource,
    private val networkDataSource: DnevnikNetworkDataSource
) : HomeworkRepository {
    override suspend fun synchronize(): Boolean = try {

        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    override val homeworkToday: Flow<List<Homework>>
        get() = flow {
            val schoolId = userDataSource.userData.first().userContext!!.school.id
            val from = LocalDateTime(2023, 3, 9, 0,0, 0, 0)
            val to = LocalDateTime(2023, 3, 9, 23,59, 0, 0)
            val homeworks = networkDataSource.getHomeworks(
                schoolId = schoolId,
                from = from,
                to = to
            )
            emit(homeworks.asExternalModelList())
        }
}