package me.injent.myschool.feature.homeworkdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.injent.myschool.core.data.downloader.Downloader
import me.injent.myschool.core.model.Attachment
import javax.inject.Inject

@HiltViewModel
class LessonDialogViewModel @Inject constructor(
    private val downloader: Downloader
) : ViewModel() {
    fun downloadFile(
        file: Attachment
    ) {
        downloader.downloadFile(
            sourceUrl = file.downloadUrl,
            preferredFileName = file.name
        )
    }
}