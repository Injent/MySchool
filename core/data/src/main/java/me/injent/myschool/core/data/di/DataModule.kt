package me.injent.myschool.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.core.data.repository.DnevnikRepository
import me.injent.myschool.core.data.repository.OfflineFirstDnevnikRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.data.repository.UserDataRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: UserDataRepositoryImpl
    ) : UserDataRepository

    @Binds
    fun bindsDnevnikRepository(
        offlineFirstDnevnikRepository: OfflineFirstDnevnikRepository
    ) : DnevnikRepository
}