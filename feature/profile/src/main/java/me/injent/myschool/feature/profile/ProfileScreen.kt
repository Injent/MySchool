package me.injent.myschool.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.model.MarkValueAndPerson

@Composable
internal fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel()
) {
//
    ProfileScreen(emptyList())
}

@Composable
internal fun ProfileScreen(list: List<MarkValueAndPerson>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(
            items = list,
            key = { _, item -> item.personId }
        ) { index, item ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = index.toString())
                Text(text = item.personName)
                Text(text = item.value.toString())
            }
        }
    }
}