package com.helios.kmptranslator.android.features.voicetranslate.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Splitscreen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.R
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.features.voicetranslate.presentation.components.VoiceState
import com.helios.kmptranslator.android.features.voicetranslate.presentation.components.VoiceTranslateBox
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.features.voicetotext.ConversationTranslateEvent
import com.helios.kmptranslator.features.voicetotext.ConversationTranslateUiState

@Composable
fun VoiceTranslateScreen(
    modifier: Modifier = Modifier,
    uiState: ConversationTranslateUiState,
    onEvent: (ConversationTranslateEvent) -> Unit,
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
            .padding(horizontal = 16.dp)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.conversation),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = {
                    onEvent(ConversationTranslateEvent.ToggleFaceToFaceMode)
                },
                content = {
                    Icon(
                        imageVector = Icons.Outlined.Splitscreen,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )

            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = {
                    onEvent(ConversationTranslateEvent.OpenHistory)
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

        VoiceTranslateBox(
            modifier = Modifier.weight(1f),
            voiceAnimationState = when (uiState.personTalking) {
                ConversationTranslateUiState.TalkingPerson.PERSON_ONE -> VoiceState.Active(uiState.powerRatio)
                else -> VoiceState.Idle
            },
            isMirrored = uiState.faceToFaceMode,
            speakingLanguage = uiState.personOne.language,
            onIdleClick = {
                onEvent(ConversationTranslateEvent.ToggleRecording(ConversationTranslateUiState.TalkingPerson.PERSON_ONE))
            },
            onActiveClick = {
                onEvent(ConversationTranslateEvent.ToggleRecording(ConversationTranslateUiState.TalkingPerson.PERSON_ONE))
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
            modifier = Modifier.weight(1f),
            voiceAnimationState = when (uiState.personTalking) {
                ConversationTranslateUiState.TalkingPerson.PERSON_TWO -> VoiceState.Active(uiState.powerRatio)
                else -> VoiceState.Idle
            },
            speakingLanguage = uiState.personTwo.language,
            onIdleClick = {
                onEvent(ConversationTranslateEvent.ToggleRecording(ConversationTranslateUiState.TalkingPerson.PERSON_TWO))
            },
            onActiveClick = {
                onEvent(ConversationTranslateEvent.ToggleRecording(ConversationTranslateUiState.TalkingPerson.PERSON_TWO))
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
            onEvent = { }
        )
    }
}