@file:Suppress("unused")
package me.injent.myschool.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.data.downloader.*
import me.injent.myschool.core.data.repository.*
import me.injent.myschool.core.data.util.ConnectivityManagerMonitor
import me.injent.myschool.core.data.util.NetworkMonitor
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @MsDownloader(MsDownloaderType.Android)
    fun providesAndroidDownloader(
        androidDownloader: AndroidDownloader
    ): Downloader

    @Binds
    @MsDownloader(MsDownloaderType.Internal)
    fun providesInternalStorageDownloader(
        internalStorageDownloader: InternalStorageDownloader
    ): Downloader

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
}