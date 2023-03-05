package me.injent.myschool.feature.students

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.topMedium
import me.injent.myschool.core.model.PersonAndMarkValue
import me.injent.myschool.core.ui.ProfilePicture

const val InlineContentId = "cup"

@Composable
internal fun StudentsRoute(
    viewModel: StudentsViewModel = hiltViewModel(),
    onPersonClick: (personId: Long) -> Unit
) {
    val students by viewModel.students.collectAsStateWithLifecycle()
    val myPlace by viewModel.myPlace.collectAsStateWithLifecycle()
    StudentsScreen(
        onPersonClick = onPersonClick,
        students = students,
        myPlace = myPlace
    )
}

@Composable
private fun StudentsScreen(
    onPersonClick: (personId: Long) -> Unit,
    students: List<PersonAndMarkValue>,
    myPlace: Int
) {
    LazyColumn(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.bg_students),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.your_place).replace("{place}", myPlace.toString()),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 32.dp, y = (-16).dp)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.topMedium
                        )
                        .align(Alignment.BottomStart)
                )
            }
        }
        // Need for cup icon
        val inlineTextContent = inlineContent()
        itemsIndexed(
            items = students,
            key = { _, item -> item.personId }
        ) { index, (personId, name, mark) ->
            StudentItem(
                onClick = { onPersonClick(personId) },
                name = name,
                mark = mark,
                place = index + 1,
                inlineContent = inlineTextContent
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun StudentItem(
    onClick: () -> Unit,
    name: String,
    mark: Float,
    place: Int,
    inlineContent: Map<String, InlineTextContent>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            onClick = {},
            shortName = name,
            modifier = Modifier
        )
        Row(modifier = Modifier.weight(1f)) {
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append(place.toString())
                    appendInlineContent(InlineContentId, "[$InlineContentId]")
                }
                append("\t")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(name)
                }
            }

            BasicText(
                text = text,
                inlineContent = inlineContent,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            text = mark.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
    }
}

private fun inlineContent(): Map<String, InlineTextContent> {
    return mapOf(
        Pair(
            InlineContentId,
            InlineTextContent(
                Placeholder(
                    width = 1.em,
                    height = 1.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {
                Icon(
                    painter = painterResource(id = MsIcons.Cup),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    )
}