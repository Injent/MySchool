package me.injent.myschool.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.injent.myschool.core.designsystem.component.AutoResizableText
import me.injent.myschool.core.designsystem.theme.Olive

@Composable
fun PlaceWidget(
    subjectName: String,
    place: Int,
    personName: String,
    modifier: Modifier = Modifier,
) {
    val painter = painterResource(if (place in 1..3) {
        R.drawable.bg_place_image_yellow
    } else {
        R.drawable.bg_place_image_white
    })
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(100.dp)
            .height(150.dp)
    ) {
        Spacer(Modifier.height(2.dp))
        Text(
            text = subjectName,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            AutoResizableText(
                text = place.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = Olive,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = personName,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
        Spacer(Modifier.height(4.dp))
    }
}