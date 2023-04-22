@file:Suppress("unused")

package me.injent.myschool.core.data.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.data.downloader.AndroidDownloader
import me.injent.myschool.core.data.downloader.Downloader
import me.injent.myschool.core.data.repository.*
import me.injent.myschool.core.data.util.ConnectivityManagerMonitor
import me.injent.myschool.core.data.util.NetworkMonitor
import me.injent.myschool.core.data.version.FirebaseVersionController
import me.injent.myschool.core.data.version.VersionController
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsUserFeedRepository(
        remoteUserFeedRepository: RemoteUserFeedRepository
    ): UserFeedRepository

    @Binds
    fun bindsPeriodRepository(
        periodRepository: RemoteReportingPeriodRepository
    ): ReportingPeriodRepository

    @Binds
    fun bindsGroupRepository(
        groupRepository: GroupRepositoryImpl
    ): GroupRepository

    @Binds
    fun bindsUserContextRepository(
        userContextRepository: OfflineFirstUserContextRepository
    ): UserContextRepository

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: UserDataRepositoryImpl
    ): UserDataRepository

    @Binds
    fun bindsPersonRepository(
        offlineFirstPersonRepository: OfflineFirstPersonRepository
    ): PersonRepository

    @Binds
    fun bindsMarkRepository(
        offlineFirstMarkRepository: OfflineFirstMarkRepository
    ): MarkRepository

    @Binds
    fun bindsSubjectRepository(
        offlineFirstSubjectRepository: OfflineFirstSubjectRepository
    ): SubjectRepository

    @Binds
    fun bindsHomeworkRepository(
        remoteHomeworkRepository: RemoteScheduleRepository
    ): ScheduleRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerMonitor,
    ): NetworkMonitor

    @Binds
    fun bindsDownloader(
        downloader: AndroidDownloader
    ): Downloader

    @Binds
    fun bindsStatisticRepository(
        statisticsRepositoryImpl: StatisticsRepositoryImpl
    ): StatisticRepository

    @Binds
    fun bindsEsiaAuthorizationRepository(
        authorizationRepositoryImpl: RemoteLoginRepository
    ): LoginRepository

    @Binds
    fun bindsVersionController(
        firebaseVersionController: FirebaseVersionController
    ): VersionController

    companion object {
        @Provides
        @Singleton
        fun providesFirestore() = Firebase.firestore
    }
}