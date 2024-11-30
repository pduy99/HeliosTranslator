package com.helios.kmptranslator.android.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbarLayout(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    initiallyCollapsed: Boolean = false,
    navigationIcon: @Composable () -> Unit = {},
    action: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val showingSubTitle by remember(topAppBarState.collapsedFraction) {
        derivedStateOf { subTitle != null && topAppBarState.collapsedFraction < 0.45f }
    }

    LaunchedEffect(initiallyCollapsed) {
        if (initiallyCollapsed) {
            topAppBarState.heightOffset = topAppBarState.heightOffsetLimit
        } else {
            topAppBarState.heightOffset = 0f
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(title, color = MaterialTheme.colorScheme.tertiary)
                        if (showingSubTitle) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                subTitle!!,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                                modifier = Modifier.alpha(
                                    (1 - (topAppBarState.collapsedFraction / 0.4f)).coerceIn(0f, 1f)
                                )
                            )
                        }

                    }
                },
                navigationIcon = {
                    navigationIcon()
                },
                actions = {
                    action()
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            content()
        }
    }
}