package me.injent.myschool.updates.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import me.injent.myschool.updates.installer.updateApkFile

/**
 * Deletes newly installed apk file to free space
 */
class UpdateInstalledBroadcast : BroadcastReceiver() {
    // Warning ignored because receiving broadcast will be handled only by this app
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        try {
            context.updateApkFile.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}