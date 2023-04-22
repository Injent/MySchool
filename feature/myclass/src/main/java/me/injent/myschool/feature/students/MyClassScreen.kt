package me.injent.myschool.feature.students

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.ui.AnimatedCollapsingContent
import me.injent.myschool.core.ui.height
import me.injent.myschool.feature.myclass.R

@Composable
internal fun MyClassRoute(
    viewModel: MyClassViewModel = hiltViewModel(),
    onPersonClick: (personId: Long) -> Unit
) {
    val myClassUiState by viewModel.myClassUiState.collectAsStateWithLifecycle()

    MyClassScreen(
        myClassUiState = myClassUiState,
        onPersonClick = onPersonClick,
        onSelectPeriod = viewModel::selectPeriod
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyClassScreen(
    myClassUiState: MyClassUiState,
    onPersonClick: (personId: Long) -> Unit,
    onSelectPeriod: (PeriodChip) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            MyClassTopAppBar(
                scrollBehavior = scrollBehavior,
                myClassUiState = myClassUiState,
                onSelectPeriod = onSelectPeriod
            )
        }
    ) { padding ->
        PersonList(
            myClassUiState = myClassUiState,
            onPersonClick = onPersonClick,
            modifier = Modifier.padding(
                top = padding.calculateTopPadding()
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyClassTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    myClassUiState: MyClassUiState,
    onSelectPeriod: (PeriodChip) -> Unit
) {
    AnimatedCollapsingContent(
        scrollBehavior = scrollBehavior,
        pinnedHeight = TopAppBarDefaults.height,
        maxHeight = 128.dp,
        motionSceneResId = R.raw.collapsing_image_with_search_scene,
        modifier = Modifier
    ) { progress ->
        val colorTransition = MaterialTheme.colorScheme.background.copy(progress)
        Image(
            modifier = Modifier
                .layoutId("background")
                .fillMaxSize()
                .drawWithContent {
                    if (progress != 1f)
                        drawContent()
                    drawRect(
                        color = colorTransition
                    )
                },
            painter = painterResource(me.injent.myschool.core.ui.R.drawable.bg_students),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )

        val myPlace = if (myClassUiState is MyClassUiState.Success) {
            "${myClassUiState.myPlace}-ое ${stringResource(R.string.your_place)}"
        } else {
            ""
        }

        Text(
            text = myPlace,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.layoutId("my_place")
        )

        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .layoutId("periods")
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            PeriodChips(
                myClassUiState = myClassUiState,
                onSelect = onSelectPeriod,
            )
        }
    }
}