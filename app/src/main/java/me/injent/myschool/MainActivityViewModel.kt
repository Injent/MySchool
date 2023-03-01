package me.injent.myschool

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.injent.myschool.core.common.SessionManager
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.data.repository.DnevnikRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.feature.authorization.AuthState
import me.injent.myschool.feature.authorization.AuthorizationViewModel.Companion.AUTH_STATE
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context
) : ViewModel() {

    @Inject
    lateinit var dnevnikRepository: dagger.Lazy<DnevnikRepository>

    val authState = savedStateHandle.getStateFlow(AUTH_STATE, AuthState.CHECKING_TOKEN)

    init {
        viewModelScope.launch {
            val token = SessionManager(context).fetchToken()
            if (token == null) {
                savedStateHandle[AUTH_STATE] = AuthState.NOT_AUTHED
            } else {
                savedStateHandle[AUTH_STATE] = AuthState.CHECKING_TOKEN

                // Checking is token not expired
                val result = dnevnikRepository.get().getContext()
                if (result is Result.Success) {
                    saveUserContext(result.data)
                } else {
                    savedStateHandle[AUTH_STATE] = AuthState.NOT_AUTHED
                }
            }
        }
    }

    /**
     * Saves [UserContext] for further access to the Dnevnik API
     */
    private suspend fun saveUserContext(userContext: UserContext) {
        savedStateHandle[AUTH_STATE] = AuthState.LOADING
        userDataRepository.setUserContext(userContext)
        savedStateHandle[AUTH_STATE] = AuthState.SUCCESS
    }
}