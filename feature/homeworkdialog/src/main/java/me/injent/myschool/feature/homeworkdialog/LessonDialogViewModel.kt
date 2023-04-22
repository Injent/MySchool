package me.injent.myschool.feature.homeworkdialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.injent.myschool.core.data.downloader.Downloader
import me.injent.myschool.core.data.downloader.OnDownloadComplitionListener
import me.injent.myschool.core.model.Attachment
import javax.inject.Inject

@HiltViewModel
class LessonDialogViewModel @Inject constructor(
    private val downloader: Downloader
) : ViewModel() {

    fun downloadFile(
        file: Attachment,
        onDownloadComplitionListener: OnDownloadComplitionListener
    ) {
        downloader.downloadFile(
            url = file.downloadUrl,
            preferredFileName = file.name,
            onDownloadComplitionListener = onDownloadComplitionListener
        )
    }
}