package me.injent.myschool.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.datastore.UserDataSerializer
import me.injent.myschool.core.model.datastore.UserData
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) dispatcher: CoroutineDispatcher
    ): DataStore<UserData> =
        DataStoreFactory.create(
            serializer = UserDataSerializer,
            scope = CoroutineScope(dispatcher + SupervisorJob())
        ) {
            context.dataStoreFile("user_preferences.pb")
        }
//    @Provides
//    @Singleton
//    fun providesEncryptedDataStore(
//        @ApplicationContext context: Context,
//        @Dispatcher(IO) dispatcher: CoroutineDispatcher
//    ) : DataStore<EncryptedData> =
//        DataStoreFactory.create(
//            serializer = EncryptedDataSerializer(CryptoManager()),
//            scope = CoroutineScope(dispatcher + SupervisorJob())
//        ) {
//            context.dataStoreFile("encrypted_data")
//        }
}