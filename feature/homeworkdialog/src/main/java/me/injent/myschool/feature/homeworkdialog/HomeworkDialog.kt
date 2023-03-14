package me.injent.myschool.feature.homeworkdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import me.injent.myschool.core.common.util.toFormattedFileSize
import me.injent.myschool.core.designsystem.component.MsTextButton
import me.injent.myschool.core.model.Attachment
import me.injent.myschool.core.model.Homework
import me.injent.myschool.core.ui.DocumentPreview
import me.injent.myschool.core.ui.Tag

@Composable
fun HomeworkDialog(
    onDismiss: () -> Unit,
    homework: Homework,
    viewModel: HomeworkDialogViewModel = hiltViewModel()
) {
    AlertDialog(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp,
        title = {
            Text(
                text = homework.subject.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            val state = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(state)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Tag(
                        text = stringResource(R.string.task),
                        containerColor = Color(0xFFffdead),
                        contentColor = Color(0xFFe1591e),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${stringResource(R.string.sent)} ${homework.teacher.shortName}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    text = homework.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Files(files = homework.files)

            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
        },
        confirmButton = {
            MsTextButton(onClick = onDismiss, text = "OK")
        }
    )
}

@Composable
private fun Files(
    files: List<Attachment>,
    viewModel: HomeworkDialogViewModel = hiltViewModel()
) {
    for (file in files) {
        var enabled by remember { mutableStateOf(true) }
        FileItem(
            file = file,
            enabled = enabled,
            onDownloadClick = {
                enabled = false
                viewModel.downloadFile(
                    file = file,
                    onDownloadComplitionListener = { success ->
                        enabled = !success
                    }
                )
            }
        )
        Divider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
private fun FileItem(
    onDownloadClick: () -> Unit,
    file: Attachment,
    enabled: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Text(
            text = "${file.name}.${file.extension}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
        DocumentPreview(
            url = file.downloadUrl,
            allowToPreview = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            Text(
                text = file.size.toFormattedFileSize(context),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            MsTextButton(
                text = stringResource(R.string.download),
                onClick = onDownloadClick,
                enabled = enabled
            )
        }
    }
}