package me.injent.myschool.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.injent.myschool.R
import me.injent.myschool.core.common.util.provideUri
import me.injent.myschool.updates.installer.openUpdateApk
import me.injent.myschool.updates.installer.updateApkFile

@Composable
fun UpdateSnackbar(state: SnackbarHostState) {
    SnackbarHost(hostState = state) {
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.has_update),
                    style = MaterialTheme.typography.bodyMedium
                )
                val context = LocalContext.current
                TextButton(onClick = { installApk(context) }) {
                    Text(
                        text = stringResource(R.string.install),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun installApk(context: Context) {
    context.openUpdateApk(context.updateApkFile.provideUri(context))
}