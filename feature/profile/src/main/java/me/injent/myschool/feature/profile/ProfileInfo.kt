package me.injent.myschool.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.topCurvedMedium
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.ui.ProfilePicture

@Composable
internal fun ProfileInfo(
    profileUiState: ProfileUiState,
    modifier: Modifier = Modifier
) {
    when (profileUiState) {
        ProfileUiState.Loading -> LoadingProfileInfo()
        is ProfileUiState.Success -> {
            ProfileInfo(
                userContext = profileUiState.profile,
                modifier = modifier
            )
        }
        ProfileUiState.Error -> Unit
    }
}

@Composable
private fun LoadingProfileInfo() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.topCurvedMedium
            )
    ) {
        ProfilePicture(
            onClick = {},
            shortName = "SN",
            modifier = Modifier
                .offset(y = (-48).dp)
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = CircleShape
                )
        )
        Text(
            text = "Sample Name",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer()
            )
        )
        Row (
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer()
            )
        ) {
            Icon(
                painter = painterResource(MsIcons.Place),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(with(LocalDensity.current) {
                    MaterialTheme.typography.bodyLarge.lineHeight.value.toDp()
                })
            )
            Text(
                text = "Sample School",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun ProfileInfo(
    userContext: UserContext,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.topCurvedMedium
            )
    ) {
        ProfilePicture(
            onClick = {},
            shortName = "${userContext.firstName} ${userContext.lastName.first()}",
            modifier = Modifier
                .offset(y = (-32).dp)
                .size(128.dp)
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
        )
        Text(
            text = "${userContext.lastName} ${userContext.firstName}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                painter = painterResource(MsIcons.Place),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = userContext.school.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}