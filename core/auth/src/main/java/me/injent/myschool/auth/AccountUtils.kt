package me.injent.myschool.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context

object AccountUtils {
    const val ACCOUNT_FULL_ACCESS = "full_access"
    const val ACCOUNT_TYPE = "me.injent.myschool"
    const val CURRENT_ACCOUNT_KEY = "current_account"
    const val KEY_ADD_ACCOUNT = "add_account"
    const val KEY_AUTH_TOKEN_TYPE = "authTokenType"

    fun getExistingAccount(context: Context): Account {
        val accountManager = AccountManager.get(context)
        val storedAccount = getStoredAccount(context)
    }

    fun getStoredAccount(context: Context): Account {

    }
}