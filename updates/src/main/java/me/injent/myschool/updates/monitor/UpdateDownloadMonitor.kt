package me.injent.myschool.updates.monitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import me.injent.myschool.updates.installer.ACTION_SEND_UPDATE_DOWNLOAD_DATA
import me.injent.myschool.updates.installer.KEY_PROGRESS
import me.injent.myschool.updates.installer.KEY_STATUS
import me.injent.myschool.updates.installer.STATUS_FAILED
import javax.inject.Inject

/**
 * Uses for receiving update downloading status and progress
 */
class UpdateDownloadMonitor @Inject constructor(
    @ApplicationContext context: Context
) {
    val updateDownloadData: Flow<UpdateDownloadData> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(
                    UpdateDownloadData(
                        status = intent.getIntExtra(KEY_STATUS, STATUS_FAILED),
                        progress = intent.getIntExtra(KEY_PROGRESS, 0)
                    )
                )
            }
        }
        context.registerReceiver(receiver, IntentFilter(ACTION_SEND_UPDATE_DOWNLOAD_DATA))

        awaitClose { context.unregisterReceiver(receiver) }
    }
        .distinctUntilChanged()
}