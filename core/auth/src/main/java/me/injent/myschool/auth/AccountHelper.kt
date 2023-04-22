package me.injent.myschool.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.injent.myschool.core.common.network.Dispatcher
import me.injent.myschool.core.common.network.MsDispatchers.IO
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.UserContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountHelper @Inject constructor(
    @ApplicationContext context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val preferencesDataSource: MsPreferencesDataSource
) {
    companion object {
        const val AUTH_TOKEN_TYPE = "full_access"
        const val ACCOUNT_TYPE = "me.injent.myschool"
        const val KEY_USERNAME = "username"
        const val KEY_AVATAR_URL = "avatar_url"
    }

    private val accountManager = AccountManager.get(context)

    val accounts: List<Account>
        get() = accountManager.getAccountsByType(ACCOUNT_TYPE).asList()

    fun getExistingAccount(): Account? {
        val storedAccount = getStoredAccount()
        if (storedAccount != null) {
            return storedAccount
        }
        return accounts.firstOrNull()
    }

    private fun getStoredAccount(): Account? {
        val lastAccount = sessionManager.getLastAccount()
        return accounts.find { it.name.toLong() == lastAccount }
    }

    fun getCurrentAccount(): Account? {
        val lastAccount = sessionManager.getLastAccount()
        if (lastAccount == 0L) return null
        return Account(lastAccount.toString(), ACCOUNT_TYPE)
    }

    private fun accountExists(account: Account): Boolean =
        accounts.any { it.name == account.name }

    suspend fun checkAuth(): AuthState = when {
        !preferencesDataSource.userData.first().isInitialized -> AuthState.NotAuthed
        getCurrentAccount() != null -> if (sessionManager.isAccessTokenExpire()) {
            AuthState.NotAuthed
        } else {
            AuthState.Success
        }
        accounts.isEmpty() -> AuthState.NotAuthed
        accounts.isNotEmpty() -> AuthState.HaveMultipleAccounts
        else -> AuthState.Error()
    }

    fun getAccountUserName(account: Account): String =
        accountManager.getUserData(account, KEY_USERNAME)

    fun getAccountAvatarUrl(account: Account): String =
        accountManager.getUserData(account, KEY_AVATAR_URL)

    fun getAccountAuthToken(account: Account): String =
        accountManager.peekAuthToken(account, AUTH_TOKEN_TYPE)

    suspend fun updateAccount(
        accessToken: String,
        userContext: UserContext,
    ) = withContext(ioDispatcher) {
        sessionManager.setAccessToken(accessToken)
        sessionManager.setLastAccount(userContext.userId)

        addAccount(
            account = Account(userContext.userId.toString(), ACCOUNT_TYPE),
            userName = "${userContext.firstName} ${userContext.lastName}",
            authToken = accessToken,
            avatarUrl = userContext.avatarUrl
        )
    }

    private fun addAccount(
        account: Account,
        userName: String,
        authToken: String,
        avatarUrl: String?
    ) {
        if (accountExists(account)) {
            updateAccount(account, authToken)
        } else {
            createAccount(account, authToken, userName, avatarUrl)
        }
    }

    private fun createAccount(account: Account, authToken: String, userName: String, avatarUrl: String?) {
        val userData = Bundle().apply {
            putString(KEY_USERNAME, userName)
            putString(KEY_AVATAR_URL, avatarUrl)
        }
        accountManager.addAccountExplicitly(account, null, userData)
        accountManager.setAuthToken(account, AUTH_TOKEN_TYPE, authToken)
    }

    private fun updateAccount(account: Account, authToken: String) {
        accountManager.setAuthToken(account, AUTH_TOKEN_TYPE, authToken)
    }
}