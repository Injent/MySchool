package me.injent.myschool.feature.updatedialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun UpdateDialog(
    viewModel: UpdateViewModel = hiltViewModel()
) {
    val update by viewModel.update.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(update) {
        showDialog = update != null
    }

    update?.let {
        UpdateDialog(
            update = it,
            onInstallClick = {
                viewModel.install()
            },
            onDismiss = { showDialog = false }
        )
    }
}

