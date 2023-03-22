package me.injent.myschool.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.data.downloader.AndroidDownloader
import me.injent.myschool.core.data.downloader.Downloader
import me.injent.myschool.core.data.repository.*
import me.injent.myschool.core.data.util.ConnectivityManagerMonitor
import me.injent.myschool.core.data.util.NetworkMonitor

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

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
        remoteHomeworkRepository: RemoteHomeworkRepository
    ): HomeworkRepository

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
}