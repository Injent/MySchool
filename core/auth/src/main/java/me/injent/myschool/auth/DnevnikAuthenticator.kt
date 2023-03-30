package me.injent.myschool.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.qualifiers.ApplicationContext
import me.injent.myschool.core.data.repository.remote.LoginRepository
import javax.inject.Inject

class DnevnikAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginRepository: LoginRepository
) : AbstractAccountAuthenticator(context) {
    override fun editProperties(p0: AccountAuthenticatorResponse?, p1: String?): Bundle {
        TODO("Not yet implemented")
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        val intent = Intent(context, LoginActivity::class.java).apply {
            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
            putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType)
            putExtra(LoginActivity.ARG_AUTH_TOKEN_TYPE, authTokenType)
            putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true)
        }

        return Bundle().apply {
            putParcelable(AccountManager.KEY_INTENT, intent)
        }
    }

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        val accountManager = AccountManager.get(context)
        val authToken = accountManager.peekAuthToken(account, authTokenType)

        if (authToken.isNullOrEmpty()) {
            val password = accountManager.getPassword(account)
            if (password != null) {
                loginRepository.authByCredentials(account.name, password)
            }

        }
    }

    override fun confirmCredentials(
        p0: AccountAuthenticatorResponse?,
        p1: Account?,
        p2: Bundle?
    ): Bundle = Bundle.EMPTY

    override fun getAuthTokenLabel(p0: String?): String = ""

    override fun updateCredentials(
        p0: AccountAuthenticatorResponse?,
        p1: Account?,
        p2: String?,
        p3: Bundle?
    ): Bundle = Bundle.EMPTY

    override fun hasFeatures(
        p0: AccountAuthenticatorResponse?,
        p1: Account?,
        p2: Array<out String>?
    ): Bundle = Bundle.EMPTY
}