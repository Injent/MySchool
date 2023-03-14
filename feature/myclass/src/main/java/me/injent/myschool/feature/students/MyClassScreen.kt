package me.injent.myschool.feature.students

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.hint
import me.injent.myschool.core.designsystem.theme.warning
import me.injent.myschool.core.model.PersonAndMarkValue
import me.injent.myschool.core.ui.CollapsingToolbar
import me.injent.myschool.core.ui.Mark
import me.injent.myschool.core.ui.ProfilePicture

@Composable
internal fun MyClassRoute(
    viewModel: MyClassViewModel = hiltViewModel(),
    onPersonClick: (personId: Long) -> Unit
) {
    val students by viewModel.students.collectAsStateWithLifecycle()
    val myPlace by viewModel.myPlace.collectAsStateWithLifecycle()
    MyClassScreen(
        onPersonClick = onPersonClick,
        classmates = students,
        myPlace = myPlace
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyClassScreen(
    onPersonClick: (personId: Long) -> Unit,
    classmates: List<PersonAndMarkValue>,
    myPlace: Int
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsingToolbar(
                scrollBehavior = scrollBehavior,
                pinnedHeight = 56.dp,
                maxHeight = 128.dp
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(
                    top = padding.calculateTopPadding()
                ),
        ) {
            itemsIndexed(
                items = classmates,
                key = { _, item -> item.personId }
            ) { index, (personId, name, mark) ->
                StudentItem(
                    onClick = { onPersonClick(personId) },
                    name = name,
                    mark = mark,
                    place = index + 1,
                    isMe = myPlace == index + 1
                )
                Divider(
                    modifier = Modifier.padding(start = 64.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
private fun StudentItem(
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
            shortName = name,
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