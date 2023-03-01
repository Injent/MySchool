package me.injent.myschool.core.common

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys

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
}