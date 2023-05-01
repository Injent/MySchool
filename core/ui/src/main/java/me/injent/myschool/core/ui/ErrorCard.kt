package me.injent.myschool.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import me.injent.myschool.core.designsystem.component.MsTextButton

@Composable
fun ErrorCard(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent,
    shape: Shape = MaterialTheme.shapes.medium
) {
    Box(
        modifier = modifier
            .background(containerColor, shape)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.failed_to_load_data)
            )
            MsTextButton(
                text = stringResource(R.string.retry),
                onClick = onRetry,
                containerColor = Color.Transparent
            )
        }
    }
}