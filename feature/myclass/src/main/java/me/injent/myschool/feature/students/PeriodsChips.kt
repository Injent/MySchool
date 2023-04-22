package me.injent.myschool.feature.students

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import me.injent.myschool.core.designsystem.component.MsFilterChip
import me.injent.myschool.core.model.PeriodType
import me.injent.myschool.feature.myclass.R

@Composable
internal fun RowScope.PeriodChips(
    myClassUiState: MyClassUiState,
    onSelect: (PeriodChip) -> Unit
) {
    when (myClassUiState) {
        MyClassUiState.Error -> Unit
        MyClassUiState.Loading -> Unit
        is MyClassUiState.Success -> {
            PeriodChips(
                periods = myClassUiState.periods,
                onSelect = onSelect,
                selectedPeriodNumber = myClassUiState.selectedPeriodNumber
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RowScope.PeriodChips(
    periods: List<PeriodChip>,
    onSelect: (PeriodChip) -> Unit,
    selectedPeriodNumber: Int
) {
    for (period in periods) {
        MsFilterChip(
            selected = period.number == selectedPeriodNumber,
            onSelected = { onSelect(period) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.background,
                selectedLabelColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.surface,
                labelColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            val context = LocalContext.current
            val periodName = remember { with(context) {
                when (period.type) {
                    PeriodType.HalfYear -> getString(R.string.half_year)
                    PeriodType.Quarter -> getString(R.string.quarter)
                    PeriodType.Semester -> getString(R.string.semester)
                    PeriodType.Trimester -> getString(R.string.trimester)
                    PeriodType.Module -> getString(R.string.module)
                }
            }}
            Text(
                text = "${period.number + 1} $periodName"
            )
        }
    }
}