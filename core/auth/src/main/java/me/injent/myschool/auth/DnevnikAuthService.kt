package me.injent.myschool.auth

import android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT
import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DnevnikAuthService : Service() {

    @Inject lateinit var authenticator: DnevnikAuthenticator

    override fun onBind(intent: Intent): IBinder? {
        return if (intent.action?.equals(ACTION_AUTHENTICATOR_INTENT) == true) {
            authenticator.iBinder
        } else null
    }
}