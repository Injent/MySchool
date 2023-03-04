package me.injent.myschool.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.OfflineFirstPersonRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.data.repository.UserDataRepositoryImpl
import me.injent.myschool.core.data.util.ConnectivityManagerMonitor
import me.injent.myschool.core.data.util.NetworkMonitor

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: UserDataRepositoryImpl
    ) : UserDataRepository

    @Binds
    fun bindsDnevnikRepository(
        offlineFirstPersonRepository: OfflineFirstPersonRepository
    ) : PersonRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerMonitor,
    ): NetworkMonitor
}