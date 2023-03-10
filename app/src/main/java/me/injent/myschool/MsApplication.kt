package me.injent.myschool

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.injent.myschool.sync.initializers.Sync

@HiltAndroidApp
class MsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Sync.initialize(this)
    }
}