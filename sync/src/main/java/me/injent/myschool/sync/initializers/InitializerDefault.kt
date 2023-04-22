package me.injent.myschool.sync.initializers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.*
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.sync.R

internal const val SilentNotificationId = 0
internal const val SilentChannelId = "Silent"

internal val Context.notificationManager: NotificationManager
    get() = this.getSystemService(NotificationManager::class.java)

internal fun Context.createSilentChannel() {
    if (SDK_INT >= 26) {
        val channel = NotificationChannel(
            SilentChannelId,
            getString(R.string.silent_channel_name),
            NotificationManager.IMPORTANCE_MIN,
        )

        notificationManager.createNotificationChannel(channel)
    }
}

class InitializerDefault : Initializer<Unit> {
    override fun create(@ApplicationContext context: Context) {
        val workerFactory = getWorkerFactory(appContext = context.applicationContext)
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, config)

        context.createSilentChannel()
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