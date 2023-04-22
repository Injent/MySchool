package me.injent.myschool.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    private val prefs: SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            "encrypted",
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    /**
     * Keys for [SharedPreferences]
     */
    companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_CURRENT_ACCOUNT = "current_account"
    }

    fun getAccessToken(): String? =
        prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getLastAccount(): Long =
        prefs.getLong(KEY_CURRENT_ACCOUNT, 0)

    fun setLastAccount(userId: Long) {
        prefs.edit {
            putLong(KEY_CURRENT_ACCOUNT, userId)
        }
    }

    fun setAccessToken(token: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, token)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun isAccessTokenExpire(): Boolean = withContext(ioDispatcher) {
        val url = URL("https://api.dnevnik.ru/v2/users/me/organizations")
        val connection = url.openConnection() as HttpURLConnection
        connection.apply {
            connectTimeout = 10000
            connection.requestMethod = "GET"
            connection.setRequestProperty("Access-Token", getAccessToken())
            connection.connect()
        }
        return@withContext connection.responseCode != 200
    }
}