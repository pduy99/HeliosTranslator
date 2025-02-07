package com.helios.sunverta.android.core.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.android.features.history.presentation.TranslateHistoryEmptyPlaceholder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CenteredTitleScrollAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    headerHeight: Dp = 200.dp,
    navigationIcon: @Composable () -> Unit = {},
    action: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val density = LocalDensity.current
    val headerHeightPx = remember(density, headerHeight) {
        with(density) { headerHeight.roundToPx() }
    }

    val lazyListState = rememberLazyListState()

    val connection = remember(headerHeightPx, lazyListState) {
        CollapsingAppBarNestedScrollConnection(headerHeightPx, lazyListState)
    }

    val headerState by remember(density, connection, headerHeightPx) {
        derivedStateOf {
            val spaceHeight = with(density) {
                (headerHeightPx + connection.appBarOffset).toDp()
            }

            val thresholdHeight = headerHeight.value * (1f / 3f)
            val effectiveSpaceHeight = spaceHeight.value - thresholdHeight
            val range = headerHeight.value - thresholdHeight
            val headerAlpha = (effectiveSpaceHeight / range).coerceIn(0f, 1f)

            val appBarAlpha = ((headerAlpha - 0.2f) / (0f - 0.2f)).coerceIn(0f, 1f)

            CollapsingHeaderState(spaceHeight, headerAlpha, appBarAlpha)
        }
    }

    Box(
        modifier = modifier.nestedScroll(connection)
    ) {
        Column {
            Spacer(
                Modifier.height(headerState.spaceHeight)
            )
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth()
            ) {
                stickyHeader {
                    val backgroundColor = MaterialTheme.colorScheme.background
                    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

                    Column(
                        modifier = Modifier.background(backgroundColor)
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = title,
                                    modifier = Modifier.alpha(headerState.appBarTitleAlpha),
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = onBackgroundColor,
                                actionIconContentColor = onBackgroundColor,
                                navigationIconContentColor = onBackgroundColor
                            ),
                            actions = action,
                            navigationIcon = navigationIcon
                        )
                        subTitle?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall.copy(color = onBackgroundColor),
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                            )
                        }
                    }
                }

                content()
            }
        }

        Box(
            modifier = Modifier
                .height(headerHeight)
                .fillMaxWidth()
                .background(Color.Transparent)
                .offset {
                    IntOffset(0, connection.appBarOffset)
                }
        ) {
            Text(
                text = title,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 32.dp)
                    .alpha(headerState.headerTitleAlpha),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Immutable
private data class CollapsingHeaderState(
    val spaceHeight: Dp,
    val headerTitleAlpha: Float,
    val appBarTitleAlpha: Float
)

private class CollapsingAppBarNestedScrollConnection(
    val appBarMaxHeight: Int,
    val lazyListState: LazyListState,
) : NestedScrollConnection {

    var appBarOffset: Int by mutableIntStateOf(0)
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (lazyListState.firstVisibleItemIndex != 0 && lazyListState.firstVisibleItemScrollOffset != 0 && available.y > 0) {
            return Offset.Zero
        }

        val delta = available.y.toInt()
        val newOffset = appBarOffset + delta
        val previousOffset = appBarOffset
        appBarOffset = newOffset.coerceIn(-appBarMaxHeight, 0)
        val consumed = appBarOffset - previousOffset
        return Offset(0f, consumed.toFloat())
    }
}

@Preview
@Composable
fun A1() {
    HeliosTranslatorTheme {
        CenteredTitleScrollAppBar(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
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
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                    )
                }
            }
        )
    }
}