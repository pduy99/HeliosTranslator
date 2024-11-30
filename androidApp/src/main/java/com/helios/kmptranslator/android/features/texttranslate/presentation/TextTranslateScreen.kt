package com.helios.kmptranslator.android.features.texttranslate.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.R
import com.helios.kmptranslator.android.core.components.LanguagePickerComponent
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.core.util.asString
import com.helios.kmptranslator.android.features.texttranslate.components.TranslateTextField
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.features.translate.TranslateEvent
import com.helios.kmptranslator.features.translate.TranslateState

@Composable
fun TextTranslateScreen(
    state: TranslateState,
    modifier: Modifier = Modifier,
    onEvent: (TranslateEvent) -> Unit,
) {
    val context = LocalContext.current
    val fromLanguage = state.fromLanguage
    val toLanguage = state.toLanguage

    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            Toast.makeText(context, it.asUiText().asString(context), Toast.LENGTH_SHORT).show()
            onEvent(TranslateEvent.OnErrorSeen)
        }
    }

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.translate),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier
                        .size(48.dp),
                    onClick = {
                        onEvent(TranslateEvent.OpenHistory)
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
        }

        item {
            LanguagePickerComponent(
                fromLanguage = fromLanguage,
                isChoosingFromLanguage = state.isChoosingFromLanguage,
                toLanguage = toLanguage,
                isChoosingToLanguage = state.isChoosingToLanguage,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
        item {
            val clipboardManager = LocalClipboardManager.current
            val keyboardController = LocalSoftwareKeyboardController.current
            TranslateTextField(
                fromText = state.fromText,
                toText = state.toText,
                isTranslating = state.isTranslating,
                fromLanguage = fromLanguage,
                toLanguage = toLanguage,
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
                modifier = Modifier.fillMaxWidth()
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