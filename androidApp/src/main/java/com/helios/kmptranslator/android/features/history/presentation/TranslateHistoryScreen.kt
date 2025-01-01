package com.helios.kmptranslator.android.features.history.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.R
import com.helios.kmptranslator.android.core.components.CenteredTitleScrollAppBar
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.features.history.components.TranslationHistoryItem
import com.helios.kmptranslator.core.presentation.UiHistoryItem
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.features.history.TranslateHistoryEvent
import com.helios.kmptranslator.features.history.TranslateHistoryUiState

@Composable
fun TranslateHistoryScreen(
    uiState: TranslateHistoryUiState,
    onEvent: (TranslateHistoryEvent) -> Unit,
    onNavigateUp: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    CenteredTitleScrollAppBar(
        title = stringResource(R.string.history),
        subTitle = if (uiState.history.isNotEmpty()) stringResource(R.string.translations_are_deleted_after_24_hours) else null,
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_up)
                )
            }
        },
        action = {
            if (uiState.history.isEmpty()) return@CenteredTitleScrollAppBar
            if (uiState.menuExpanded) {
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                        .clickable {
                            onEvent(TranslateHistoryEvent.DeleteAllHistory)
                        }
                ) {
                    Text(
                        text = stringResource(R.string.delete_history),
                        modifier = Modifier.padding(
                            start = 28.dp,
                            top = 14.dp,
                            bottom = 14.dp,
                            end = 42.dp
                        )
                    )
                }
            } else {
                IconButton(onClick = {
                    onEvent(TranslateHistoryEvent.ToggleMenu())
                }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                    )
                }
            }
        },
        content = {
            if (uiState.history.isNotEmpty()) {
                items(uiState.history) { historyItem ->
                    TranslationHistoryItem(
                        historyItem = historyItem,
                        onCopyClick = {
                            clipboardManager.setText(
                                buildAnnotatedString { append(it) }
                            )
                            Toast.makeText(
                                context,
                                context.getString(R.string.copied_to_clipboard),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            } else {
                item {
                    TranslateHistoryEmptyPlaceholder(modifier = Modifier.fillParentMaxSize())
                }
            }
        }
    )
}

@Composable
fun TranslateHistoryEmptyPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_items),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.translations_are_deleted_after_24_hours),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Preview
@Composable
fun A1() {
    HeliosTranslatorTheme {
        TranslateHistoryScreen(
            uiState = TranslateHistoryUiState(),
            onNavigateUp = {},
            onEvent = {})
    }
}

@Preview
@Composable
fun A2() {
    HeliosTranslatorTheme {
        TranslateHistoryScreen(
            uiState = TranslateHistoryUiState(
                history = listOf(
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    )
                )
            ),
            onNavigateUp = {},
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun A3() {
    HeliosTranslatorTheme {
        TranslateHistoryScreen(
            uiState = TranslateHistoryUiState(
                history = listOf(
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    ),
                    UiHistoryItem(
                        0,
                        "Hello",
                        "Gracias",
                        fromLanguage = UiLanguage.byCode("en"),
                        toLanguage = UiLanguage.byCode("es"),
                    )
                ),
                menuExpanded = true
            ),
            onNavigateUp = {},
            onEvent = {}
        )
    }
}