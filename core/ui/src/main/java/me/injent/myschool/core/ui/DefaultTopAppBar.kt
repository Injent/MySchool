package me.injent.myschool.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.injent.myschool.core.designsystem.component.AutoResizableText
import me.injent.myschool.core.designsystem.icon.MsIcons

@OptIn(ExperimentalMaterial3Api::class)
val TopAppBarDefaults.height: Dp
    @Composable get() = 56.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    onBack: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    titleColor: Color = MaterialTheme.colorScheme.onBackground
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = titleColor
        ),
        title = {
            AutoResizableText(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = MsIcons.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
        actions = actions,
    )
}