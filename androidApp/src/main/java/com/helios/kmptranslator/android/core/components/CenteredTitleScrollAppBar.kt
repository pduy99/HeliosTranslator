package com.helios.kmptranslator.android.core.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.features.history.presentation.TranslateHistoryEmptyPlaceholder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CenteredTitleScrollAppBar(
    title: String,
    subTitle: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    action: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val lazyListScrollState = rememberLazyListState()

    // Alpha for header title and app bar title
    val headerTitleAlpha by remember(lazyListScrollState) {
        derivedStateOf {
            val offset = lazyListScrollState.firstVisibleItemScrollOffset
            val isFirstItem = lazyListScrollState.firstVisibleItemIndex == 0

            if (isFirstItem) 1f - (offset / 400f).coerceIn(0f, 1f) else 0f
        }
    }

    val appBarTitleAlpha by remember(lazyListScrollState) {
        derivedStateOf {
            val offset = lazyListScrollState.firstVisibleItemScrollOffset
            val isFirstItem = lazyListScrollState.firstVisibleItemIndex == 0

            if (isFirstItem) (offset - 400f).coerceAtLeast(0f) / 400f else 1f
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = lazyListScrollState
    ) {
        // Header with large title
        item {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .alpha(headerTitleAlpha),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        // Sticky AppBar
        stickyHeader {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            modifier = Modifier.alpha(appBarTitleAlpha)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    actions = action,
                    navigationIcon = navigationIcon
                )
                subTitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                    )
                }
            }
        }

        content()
    }
}

@Preview
@Composable
fun A1() {
    HeliosTranslatorTheme {
        CenteredTitleScrollAppBar(
            title = "Title",
            subTitle = "Translations are deleted after 24 hours",
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            action = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                    )
                }
            },
            content = {
                item {
                    TranslateHistoryEmptyPlaceholder(modifier = Modifier.fillParentMaxSize())
                }
            }
        )
    }
}

@Preview
@Composable
fun A2() {
    HeliosTranslatorTheme {
        CenteredTitleScrollAppBar(
            title = "Title",
            subTitle = "Translations are deleted after 24 hours",
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            action = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                    )
                }
            },
            content = {
                items(25) { index ->
                    ListItem(
                        headlineContent = { Text("Item $index") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
        )
    }
}