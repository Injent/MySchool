package me.injent.myschool.sync.initializers

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.*
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Use it to initialize sync work.
 * Used only in Application.onCreate method
 */
//object Sync {
//    fun initialize(context: Context) {
//        AppInitializer.getInstance(context)
//            .initializeComponent(SyncInitializer::class.java)
//    }
//}

class SyncInitializer : Initializer<Unit> {
    override fun create(@ApplicationContext context: Context) {
        val workerFactory = getWorkerFactory(appContext = context.applicationContext)
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, config)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    private fun getWorkerFactory(appContext: Context): HiltWorkerFactory {
        val workManagerEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            WorkManagerInitializerEntryPoint::class.java
        )
        return workManagerEntryPoint.hiltWorkerFactory()
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface WorkManagerInitializerEntryPoint {
        fun hiltWorkerFactory(): HiltWorkerFactory
    }
}