package me.injent.myschool.feature.updatedialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.injent.myschool.core.data.version.VersionController
import javax.inject.Inject

class UpdateViewModel @Inject constructor(
    private val updateInstaller: UpdateInstaller,
    private val versionController: VersionController
) : ViewModel() {

    val update = flow {
        emit(versionController.getUpdate())
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun install() {
        if (update.value != null)
            viewModelScope.launch {
                updateInstaller.update(update.value!!.url)
            }
    }
}