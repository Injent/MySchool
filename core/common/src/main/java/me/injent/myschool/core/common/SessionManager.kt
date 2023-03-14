package me.injent.myschool.core.common

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.HttpURLConnection
import java.net.URL

class SessionManager(context: Context) {

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
     * Keys for SharedPreferences
     */
    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Saves token with encryption
     * @param token represents Access-Token which use in API requests
     */
    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Gets decrypted token
     * @return token represents Access-Token which use in API requests
     */
    fun fetchToken(): String? {
        return try {
            prefs.getString(USER_TOKEN, null)
        } catch (e: Exception) {
            null
        }
    }

    val isTokenActive: Flow<Boolean>
        get() = flow {
            try {
                val token = fetchToken() ?: kotlin.run {
                    emit(false)
                    return@flow
                }
                val url = URL("https://api.dnevnik.ru/v2/users/me/organizations")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Access-Token", token)
                connection.connect()
                emit(connection.responseCode == 200)
            } catch (e: Exception) {
                emit(false)
            }
        }
            .flowOn(Dispatchers.IO)
}