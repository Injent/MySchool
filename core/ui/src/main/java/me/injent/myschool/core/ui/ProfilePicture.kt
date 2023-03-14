package me.injent.myschool.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfilePicture(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shortName: String
) {
    Box(
        modifier = modifier
            .requiredSize(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = "${shortName.first()}${shortName.last()}",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun LoadingProfilePicture(
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .requiredSize(48.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
    )
}

@Composable
fun FailedProfilePicture(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .requiredSize(48.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}