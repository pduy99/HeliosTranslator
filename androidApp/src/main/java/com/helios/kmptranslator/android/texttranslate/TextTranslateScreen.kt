package com.helios.kmptranslator.android.texttranslate

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.helios.kmptranslator.android.texttranslate.components.TranslateTextField
import com.helios.kmptranslator.android.texttranslate.components.TranslationHistoryItem
import com.helios.kmptranslator.android.texttranslate.presentation.asUiText
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.translate.TranslateEvent
import com.helios.kmptranslator.translate.TranslateState

@Composable
fun TextTranslateScreen(
    state: TranslateState,
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
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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

        item {
            if (state.history.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.history),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }

        items(state.history) { historyItem ->
            TranslationHistoryItem(
                historyItem = historyItem,
                onClick = { onEvent(TranslateEvent.SelectHistoryTranslationItem(historyItem)) },
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