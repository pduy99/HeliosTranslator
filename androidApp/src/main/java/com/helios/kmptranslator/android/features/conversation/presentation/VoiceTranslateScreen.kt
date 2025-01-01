package com.helios.kmptranslator.android.features.conversation.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Splitscreen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.core.components.AppBar
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.features.conversation.presentation.components.VoiceState
import com.helios.kmptranslator.android.features.conversation.presentation.components.VoiceTranslateBox
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.features.voicetotext.ConversationTranslateEvent
import com.helios.kmptranslator.features.voicetotext.ConversationTranslateUiState

@Composable
fun VoiceTranslateScreen(
    modifier: Modifier = Modifier,
    uiState: ConversationTranslateUiState,
    onEvent: (ConversationTranslateEvent) -> Unit,
    onNavigateUp: () -> Unit
) {
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onEvent(ConversationTranslateEvent.PermissionResult(isGranted))
    }

    LaunchedEffect(recordAudioPermissionLauncher) {
        recordAudioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppBar(
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(
                    onClick = onNavigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Saved translate"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        onEvent(ConversationTranslateEvent.ToggleFaceToFaceMode)
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.Splitscreen,
                            contentDescription = "Face to face mode",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                )
            }
        )

        VoiceTranslateBox(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            voiceAnimationState = when (uiState.personTalking) {
                ConversationTranslateUiState.TalkingPerson.PERSON_ONE -> VoiceState.Active(
                    uiState.powerRatio
                )

                else -> VoiceState.Idle
            },
            isMirrored = uiState.faceToFaceMode,
            speakingLanguage = uiState.personOne.language,
            onIdleClick = {
                onEvent(
                    ConversationTranslateEvent.ToggleRecording(
                        ConversationTranslateUiState.TalkingPerson.PERSON_ONE
                    )
                )
            },
            onActiveClick = {
                onEvent(
                    ConversationTranslateEvent.ToggleRecording(
                        ConversationTranslateUiState.TalkingPerson.PERSON_ONE
                    )
                )
            },
            isChoosingLanguage = uiState.personOne.isChoosingLanguage,
            onLanguageDropdownClick = {
                onEvent(
                    ConversationTranslateEvent.OpenLanguageDropDown(
                        ConversationTranslateUiState.TalkingPerson.PERSON_ONE
                    )
                )
            },
            onLanguageDropDownDismiss = { onEvent(ConversationTranslateEvent.StopChoosingLanguage) },
            onSelectLanguage = {
                onEvent(
                    ConversationTranslateEvent.ChooseLanguage(
                        ConversationTranslateUiState.TalkingPerson.PERSON_ONE,
                        it
                    )
                )
            },
            content = uiState.personOne.text
        )

        VoiceTranslateBox(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            voiceAnimationState = when (uiState.personTalking) {
                ConversationTranslateUiState.TalkingPerson.PERSON_TWO -> VoiceState.Active(
                    uiState.powerRatio
                )

                else -> VoiceState.Idle
            },
            speakingLanguage = uiState.personTwo.language,
            onIdleClick = {
                onEvent(
                    ConversationTranslateEvent.ToggleRecording(
                        ConversationTranslateUiState.TalkingPerson.PERSON_TWO
                    )
                )
            },
            onActiveClick = {
                onEvent(
                    ConversationTranslateEvent.ToggleRecording(
                        ConversationTranslateUiState.TalkingPerson.PERSON_TWO
                    )
                )
            },
            isChoosingLanguage = uiState.personTwo.isChoosingLanguage,
            onLanguageDropdownClick = {
                onEvent(
                    ConversationTranslateEvent.OpenLanguageDropDown(
                        ConversationTranslateUiState.TalkingPerson.PERSON_TWO
                    )
                )
            },
            onLanguageDropDownDismiss = { onEvent(ConversationTranslateEvent.StopChoosingLanguage) },
            onSelectLanguage = {
                onEvent(
                    ConversationTranslateEvent.ChooseLanguage(
                        ConversationTranslateUiState.TalkingPerson.PERSON_TWO,
                        it
                    )
                )
            },
            content = uiState.personTwo.text
        )
    }
}

@Preview
@Composable
fun VoiceToTextPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        VoiceTranslateScreen(
            uiState = ConversationTranslateUiState(
                personOne = ConversationTranslateUiState.PersonState(language = UiLanguage.byCode("en")),
                personTwo = ConversationTranslateUiState.PersonState(language = UiLanguage.byCode("ja"))
            ),
            onNavigateUp = {},
            onEvent = {}
        )
    }
}