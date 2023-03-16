package me.injent.myschool.feature.students

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import me.injent.myschool.core.ui.AnimatedCollapsingContent
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyClassScreen(
    myClassUiState: MyClassUiState,
    onPersonClick: (personId: Long) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .systemBarsPadding(),
        topBar = {
            MyClassTopAppBar(
                scrollBehavior = scrollBehavior,
                myClassUiState = myClassUiState
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
    myClassUiState: MyClassUiState
) {
    AnimatedCollapsingContent(
        scrollBehavior = scrollBehavior,
        pinnedHeight = 56.dp,
        maxHeight = 128.dp,
        motionSceneResId = R.raw.collapsing_image_with_search_scene
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

        val context = LocalContext.current
        val myPlaceText = remember(myClassUiState) {
            if (myClassUiState is MyClassUiState.Success) {
                context.getString(R.string.your_place)
                    .replace("{place}", myClassUiState.myPlace.toString())
            } else {
                null
            }
        }
        Text(
            text = myPlaceText ?: context.getString(R.string.your_place),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .layoutId("my_place")
                .placeholder(
                    visible = myPlaceText == null,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Box(
            modifier = Modifier
                .layoutId("search")
                .fillMaxWidth(0.5f)
                .height(36.dp)
                .background(Color.Black.copy(.15f), CircleShape)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Поиск",
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .align(Alignment.CenterStart)
            )
        }
    }
}