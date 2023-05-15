package me.injent.myschool.feature.students

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.warning
import me.injent.myschool.core.domain.model.PersonWithAverageMark
import me.injent.myschool.core.ui.MarkView
import me.injent.myschool.core.ui.ProfilePicture

@Composable
fun PersonList(
    myClassUiState: MyClassUiState,
    onPersonClick: (personId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (myClassUiState) {
        MyClassUiState.Loading -> { LoadingPersonList(modifier) }
        is MyClassUiState.Success -> {
            Surface(
                modifier = modifier
            ) {
                LazyColumn {
                    itemsIndexed(
                        items = myClassUiState.personsAndMarks,
                        key = { _, item -> item.personId }
                    ) { index, personAndMarkValue ->
                        Person(
                            onClick = { onPersonClick(personAndMarkValue.personId) },
                            personWithAverageMark = personAndMarkValue,
                            place = index + 1,
                            isMe = myClassUiState.myPlace == index + 1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        MyClassUiState.Error -> {}
    }
}

@Composable
private fun Person(
    onClick: () -> Unit,
    personWithAverageMark: PersonWithAverageMark,
    place: Int,
    isMe: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
) {
    ConstraintLayout(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(contentPadding)
    ) {
        val (avatar, placeText, cupIcon, name, markValue) = createRefs()

        ProfilePicture(
            avatarUrl = personWithAverageMark.avatarUrl,
            name = personWithAverageMark.personName,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(avatar) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        val fontStyle = MaterialTheme.typography.bodyMedium

        Text(
            text = personWithAverageMark.personName,
            style = fontStyle,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(avatar.end, 16.dp)
                top.linkTo(parent.top)
            }
        )

        Icon(
            painter = painterResource(MsIcons.Cup),
            contentDescription = null,
            tint = if (isMe) {
                MaterialTheme.colorScheme.warning
            } else {
                MaterialTheme.colorScheme.secondary
            },
            modifier = Modifier
                .size(fontStyle.fontSize.value.dp)
                .constrainAs(cupIcon) {
                    start.linkTo(name.start)
                    top.linkTo(name.bottom, 2.dp)
                }
        )

        Text(
            text = place.toString(),
            style = fontStyle,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.constrainAs(placeText) {
                start.linkTo(cupIcon.end)
                top.linkTo(cupIcon.top)
                bottom.linkTo(cupIcon.bottom)
            }
        )

        MarkView(
            value = personWithAverageMark.averageMarkValue.toString(),
            alpha = .15f,
            modifier = Modifier
                .clip(CircleShape)
                .constrainAs(markValue) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun LoadingPersonList(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
    ) {
        Column {
            repeat(20) {
                LoadingPersonItem(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun LoadingPersonItem(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
) {
    ConstraintLayout(
        modifier = modifier.padding(contentPadding)
    ) {
        val placeholder = Modifier.placeholder(
            visible = true,
            highlight = PlaceholderHighlight.shimmer()
        )
        val (avatar, placeText, cupIcon, name, markValue) = createRefs()

        ProfilePicture(
            avatarUrl = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .then(placeholder)
                .constrainAs(avatar) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        val fontStyle = MaterialTheme.typography.bodyMedium

        Text(
            text = "Person Name",
            style = fontStyle,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .then(placeholder)
                .constrainAs(name) {
                    start.linkTo(avatar.end, 16.dp)
                    top.linkTo(parent.top)
                }
        )
        Text(
            text = "",
            style = fontStyle,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .constrainAs(placeText) {
                    start.linkTo(cupIcon.end)
                    top.linkTo(cupIcon.top)
                    bottom.linkTo(cupIcon.bottom)
                }
        )
        MarkView(
            value = "#.##",
            modifier = Modifier
                .clip(CircleShape)
                .then(placeholder)
                .constrainAs(markValue) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}