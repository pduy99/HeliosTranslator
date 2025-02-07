package com.helios.sunverta.android.features.text.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helios.sunverta.android.R
import com.helios.sunverta.android.core.components.AppBar
import com.helios.sunverta.android.core.components.LanguagePickerComponent
import com.helios.sunverta.android.core.components.imeVisibilityAsState
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.android.core.util.asString
import com.helios.sunverta.android.features.text.components.TranslateTextField
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.features.translate.TranslateEvent
import com.helios.sunverta.features.translate.TranslateState

@Composable
fun TextTranslateScreen(
    state: TranslateState,
    modifier: Modifier = Modifier,
    onEvent: (TranslateEvent) -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val isKeyboardOpen by imeVisibilityAsState()
    val density = LocalDensity.current

    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            Toast.makeText(context, it.asUiText().asString(context), Toast.LENGTH_SHORT).show()
            onEvent(TranslateEvent.OnErrorSeen)
        }
    }

    LaunchedEffect(isKeyboardOpen) {
        if (!isKeyboardOpen) {
            focusManager.clearFocus(true)
        }
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onEvent(TranslateEvent.OpenHistoryScreen)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = "Saved translate"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.inversePrimary),
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = "Saved translate",
                            )
                        }
                    }
                )
            }

            item {
                LanguagePickerComponent(
                    fromLanguage = state.fromLanguage,
                    isChoosingFromLanguage = state.isChoosingFromLanguage,
                    toLanguage = state.toLanguage,
                    isChoosingToLanguage = state.isChoosingToLanguage,
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
            item {
                TranslateTextField(
                    fromText = state.fromText,
                    toText = state.toText,
                    isTranslating = state.isTranslating,
                    fromLanguage = state.fromLanguage,
                    toLanguage = state.toLanguage,
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.Translate)
                    },
                    onTextChanged = { onEvent(TranslateEvent.ChangeTranslationText(it)) },
                    onCopyClick = {
                        clipboardManager.setText(
                            buildAnnotatedString {
                                append(it)
                            }
                        )
                        Toast.makeText(
                            context,
                            context.getString(R.string.copied_to_clipboard),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onCloseClick = { onEvent(TranslateEvent.CloseTranslation) },
                    onSpeakerClick = {
                        onEvent(TranslateEvent.ReadAloudText)
                    },
                    onTextFieldClick = { onEvent(TranslateEvent.EditTranslation) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp),
            visible = !isKeyboardOpen,
            enter = slideInVertically {
                with(density) { -40.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
        ) {
            MainFunctionsFAB(
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun MainFunctionsFAB(
    modifier: Modifier = Modifier,
    onEvent: (TranslateEvent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(46.dp)
                    .background(Color(0xFF404759)),
                onClick = {},
            ) {
                Icon(
                    imageVector = Icons.Outlined.FolderOpen,
                    contentDescription = null,
                    tint = Color(0xFFDDE2FF)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Gallery",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(2f)
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)

                    .size(90.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                onClick = {
                    onEvent(TranslateEvent.OpenCameraTranslateScreen)
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    modifier = Modifier.size(30.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .background(Color(0xFF404759)),
                onClick = {
                    onEvent(TranslateEvent.OpenConversationTranslateScreen)
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.PeopleOutline,
                    contentDescription = null,
                    tint = Color(0xFFDDE2FF)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Conversation",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )
        }
    }

}

@Preview
@Composable
fun TextTranslateScreenPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        TextTranslateScreen(
            state = TranslateState(
                fromText = "Hello",
                toText = "お問い合わせ",
                isTranslating = false,
                fromLanguage = UiLanguage.byCode("en"),
                toLanguage = UiLanguage.byCode("ja"),
                isChoosingFromLanguage = false,
                isChoosingToLanguage = false,
                error = null,
                history = emptyList()
            ),
            onEvent = {}
        )
    }
}