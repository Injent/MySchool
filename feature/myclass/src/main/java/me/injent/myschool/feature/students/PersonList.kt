package me.injent.myschool.feature.students

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.hint
import me.injent.myschool.core.designsystem.theme.warning
import me.injent.myschool.core.model.PersonAndMarkValue
import me.injent.myschool.core.ui.Mark
import me.injent.myschool.core.ui.ProfilePicture

sealed interface MyClassUiState {
    object Loading : MyClassUiState
    object Error : MyClassUiState
    data class Success(
        val myPlace: Int,
        val personsAndMarks: List<PersonAndMarkValue>
    ) : MyClassUiState
}

@Composable
fun PersonList(
    myClassUiState: MyClassUiState,
    onPersonClick: (personId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (myClassUiState) {
        MyClassUiState.Loading -> { LoadingPersonList(modifier) }
        is MyClassUiState.Success -> {
            LazyColumn(
                modifier = modifier.background(MaterialTheme.colorScheme.surface),
            ) {
                itemsIndexed(
                    items = myClassUiState.personsAndMarks,
                    key = { _, item -> item.personId }
                ) { index, (personId, name, mark) ->
                    PersonItem(
                        onClick = { onPersonClick(personId) },
                        name = name,
                        mark = mark,
                        place = index + 1,
                        isMe = myClassUiState.myPlace == index + 1
                    )
                    Divider(
                        modifier = Modifier.padding(start = 64.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
        MyClassUiState.Error -> {}
    }
}

@Composable
private fun PersonItem(
    onClick: () -> Unit,
    name: String,
    mark: Float,
    place: Int,
    isMe: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = MaterialTheme.colorScheme.hint),
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            onClick = onClick,
            shortName = name
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                if (place < 10) {
                    withStyle(SpanStyle(color = Color.Transparent)) { append("0") }
                }
                append(place.toString())
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Icon(
            painter = painterResource(id = MsIcons.Cup),
            contentDescription = null,
            tint = if (isMe) {
                MaterialTheme.colorScheme.warning
            } else {
                MaterialTheme.colorScheme.secondary
            },
            modifier = Modifier.size(MaterialTheme.typography.titleMedium.fontSize.value.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Box(Modifier.weight(1f)) {
            Mark(
                value = mark.toString(),
                alpha = .1f,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun LoadingPersonList(modifier: Modifier = Modifier) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        repeat(20) {
            LoadingPersonItem()
            Divider(
                modifier = Modifier.padding(start = 64.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
private fun LoadingPersonItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            onClick = {},
            shortName = "NS",
            modifier = Modifier
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))

        val fontStyle = MaterialTheme.typography.bodyMedium
        Text(
            text = "00  #",
            style = fontStyle,
            modifier = Modifier.placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer()
            )
        )
        Text(
            text = "Sample N.",
            style = fontStyle,
            modifier = Modifier
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Box(Modifier.weight(1f)) {
            Mark(
                value = "0.00",
                alpha = .1f,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
        }
    }
}