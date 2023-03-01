package me.injent.myschool.core.data.repository

import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.ExternalUserProfile
import me.injent.myschool.core.model.ShortUserInfo
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.model.asExternalModel
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.common.result.Result.Error
import me.injent.myschool.core.common.result.Result.Success
import me.injent.myschool.core.model.ReportingPeriod
import javax.inject.Inject

/**
 * Repository to work with Dnevnik Api
 * Note: use it only after getting Access-Token
 */
interface DnevnikRepository : Synchronizable {
    /**
     * Gets current user profile information
     * Also it uses for token expiration check
     */
    suspend fun getContext(): Result<UserContext>

    /**
     * @param userId refers to [UserContext.userId] or [ShortUserInfo.userId]
     * Gets external user profile information
     */
    suspend fun getExternalUserProfile(userId: Long): ExternalUserProfile

    /**
     * @param eduGroupId refers to [EduGroup.id]
     * Gets list of [ShortUserInfo] which represents short info of every
     * person in Education Group
     */
    suspend fun getPersonsInEduGroup(eduGroupId: Long): List<ShortUserInfo>

    /**
     * @param eduGroupId refers to [EduGroup.id]
     * Gets list of [ReportingPeriod] which represents quarters, semesters, half-years
     * in specific Education Group
     */
    suspend fun getReportingPeriods(eduGroupId: Long): List<ReportingPeriod>
}

/**
 * Offline-First repository is a repository
 * that is able to perform all, or a critical subset of its core
 * functionality without access to the internet. That is, it can perform some or all of its
 * business logic offline.
 */
class OfflineFirstDnevnikRepository @Inject constructor(
    private val networkDataSource: DnevnikNetworkDataSource
) : DnevnikRepository {

    override suspend fun synchronize() {
        TODO("Write sync logic")
    }

    override suspend fun getContext(): Result<UserContext> {
        val result = networkDataSource.getUserContext()
        return if (result is Success) {
            Success(result.data.asExternalModel())
        } else {
            // There is no way Result.Loading can be received
            // so the force cast is used here
            Error((result as Error).exception)
        }
    }

    override suspend fun getExternalUserProfile(userId: Long)
        = networkDataSource.getExternalUserProfile(userId).asExternalModel()

    override suspend fun getPersonsInEduGroup(eduGroupId: Long)
        = networkDataSource.getPersonsInEduGroup(eduGroupId).map { it.asExternalModel() }

    override suspend fun getReportingPeriods(eduGroupId: Long)
        = networkDataSource.getReportingPeriods(eduGroupId).map { it.asExternalModel() }
}