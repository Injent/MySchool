package me.injent.myschool.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Subject
import me.injent.myschool.core.ui.ProfilePicture

internal fun LazyGridScope.bestStudents(
    bestStudents: Map<Subject, Person>,
    modifier: Modifier = Modifier
) {
    item {
        Text(
            text = stringResource(R.string.best_students_by_subjects),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
    item {
        Spacer(Modifier.height(8.dp))
    }
    item {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .height(140.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            items(bestStudents.toList()) { (subject, person) ->
                StudentWidget(
                    subjectName = subject.name,
                    person = person
                )
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight(),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
private fun StudentWidget(
    subjectName: String,
    person: Person,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .width(100.dp)
            .height(140.dp)
    ) {
        val (subjectText, picture, nameText) = createRefs()

        Text(
            text = subjectName,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.constrainAs(subjectText) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, 4.dp)
            }
        )
        ProfilePicture(
            avatarUrl = person.avatarUrl,
            name = person.shortName,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(64.dp)
                .constrainAs(picture) {
                    top.linkTo(subjectText.bottom, 4.dp)
                    bottom.linkTo(nameText.top)
                }
        )
        Text(
            text = person.shortName,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .constrainAs(nameText) {
                    start.linkTo(parent.start, 4.dp)
                    end.linkTo(parent.end, 4.dp)
                    bottom.linkTo(parent.bottom, 4.dp)
                }
        )
    }
}