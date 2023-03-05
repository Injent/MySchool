package me.injent.myschool.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.hint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    onBack: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    personShortName: String,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.hint,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = titleColor
        ),
        title = {
            Box {
                Text(
                    text = personShortName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = MsIcons.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.hint
                )
            }
        },
        actions = {
            ProfilePicture(
                shortName = personShortName,
                onClick = onProfileClick
            )
        }
    )
}