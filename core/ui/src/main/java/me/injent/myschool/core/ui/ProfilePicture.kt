package me.injent.myschool.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.injent.myschool.core.designsystem.component.DynamicAsyncImage

@Composable
fun ProfilePicture(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    name: String? = null,
) {
    var showPictureDialog by remember { mutableStateOf(false) }

    if (showPictureDialog && name != null) {
        Dialog(onDismissRequest = { showPictureDialog = false }) {
            Box(Modifier.size(256.dp)) {
                DynamicAsyncImage(
                    imageUrl = avatarUrl!!,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.avatar_placeholder),
                    modifier = Modifier.matchParentSize()

                )
                Surface(
                    color = Color.Black.copy(.5f),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = name,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                }
            }
        }
    }

    if (!avatarUrl.isNullOrEmpty()) {
        DynamicAsyncImage(
            imageUrl = avatarUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.avatar_placeholder),
            modifier = modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable {
                    showPictureDialog = true
                }
        )
    } else {
        Image(
            painter = painterResource(R.drawable.avatar_placeholder),
            contentDescription = null,
            modifier = modifier
                .size(32.dp)
                .clip(CircleShape)
        )
    }
}