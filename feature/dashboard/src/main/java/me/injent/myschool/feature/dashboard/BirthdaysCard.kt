package me.injent.myschool.feature.dashboard

import android.icu.text.RelativeDateTimeFormatter
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import me.injent.myschool.core.common.util.BIRTHDAY_DATE_FORMAT
import me.injent.myschool.core.common.util.format
import me.injent.myschool.core.model.Birthday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BirthdayCard(
    birthday: Birthday,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Box(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("${stringResource(R.string.birthday)} у ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("${birthday.personName} ")
                    }
                    if (SDK_INT >= 24) {
                        append(
                            if (birthday.daysUntil > 0) {
                                RelativeDateTimeFormatter.getInstance().format(
                                    birthday.daysUntil.toDouble(),
                                    RelativeDateTimeFormatter.Direction.NEXT,
                                    RelativeDateTimeFormatter.RelativeUnit.DAYS
                                )
                            } else {
                                stringResource(R.string.birthday_today)
                            }
                        )
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append("\n(${birthday.date.format(BIRTHDAY_DATE_FORMAT)})")
                    }
                },
                maxLines = 5,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}