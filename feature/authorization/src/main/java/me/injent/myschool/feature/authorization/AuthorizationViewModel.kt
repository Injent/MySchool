package me.injent.myschool.feature.authorization

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.injent.myschool.core.common.SessionManager
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.common.result.Result.Success
import me.injent.myschool.core.data.repository.DnevnikRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    @Inject
    lateinit var dnevnikRepository: dagger.Lazy<DnevnikRepository>

    companion object {
        const val AUTH_STATE = "auth_state"
        private const val TOKEN = "token"
    }

    val authState = savedStateHandle.getStateFlow(AUTH_STATE, AuthState.CHECKING_TOKEN)
    /**
     * Received token from deeplink
     */
    private val token: String? = savedStateHandle[TOKEN]

    init {
        if (token != null) {
            savedStateHandle[AUTH_STATE] = AuthState.LOADING
            viewModelScope.launch {
                SessionManager(context).saveToken(token)
                delay(100)
                val result = dnevnikRepository.get().getContext()
                if (result is Success) {
                    userDataRepository.setUserContext(result.data)
                    savedStateHandle[AUTH_STATE] = AuthState.SUCCESS
                } else {
                    savedStateHandle[AUTH_STATE] = AuthState.NOT_AUTHED
                }
            }
        }
    }
}