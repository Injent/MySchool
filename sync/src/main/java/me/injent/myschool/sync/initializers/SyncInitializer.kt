package me.injent.myschool.sync.initializers

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.*
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.sync.workers.SyncWorker
import javax.inject.Singleton

/**
 * Use it to initialize sync work.
 * Used only in Application.onCreate method
 */
object Sync {
    fun initialize(context: Context) {
        AppInitializer.getInstance(context)
            .initializeComponent(SyncInitializer::class.java)
    }
}

const val WORKER_NAME = "synchronization"

/**
 * Followed by this guide
 * [Android Developers](https://developer.android.com/topic/architecture/data-layer/offline-first)
**/
object SyncInitializer : Initializer<WorkManager> {

    override fun create(@ApplicationContext context: Context): WorkManager {
        val workerFactory = getWorkerFactory(appContext = context.applicationContext)
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, config)
        return WorkManager.getInstance(context)
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