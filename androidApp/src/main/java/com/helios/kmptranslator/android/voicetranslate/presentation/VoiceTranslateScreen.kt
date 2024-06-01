package com.helios.kmptranslator.android.voicetranslate.presentation

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition.Companion.Center
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.R
import com.helios.kmptranslator.android.core.theme.LightBlue
import com.helios.kmptranslator.android.voicetranslate.presentation.components.VoiceRecorderDisplay
import com.helios.kmptranslator.voicetotext.DisplayState
import com.helios.kmptranslator.voicetotext.VoiceToTextEvent
import com.helios.kmptranslator.voicetotext.VoiceToTextState

@Composable
fun VoiceTranslateScreen(
    state: VoiceToTextState,
    languageCode: String,
    onResult: (String) -> Unit,
    onEvent: (VoiceToTextEvent) -> Unit
) {

    val context = LocalContext.current
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onEvent(
            VoiceToTextEvent.PermissionResult(
                isGranted,
                !isGranted && (context as ComponentActivity).shouldShowRequestPermissionRationale(
                    android.Manifest.permission.RECORD_AUDIO
                )
            )
        )
    }

    LaunchedEffect(recordAudioPermissionLauncher) {
        recordAudioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    Scaffold(
        floatingActionButtonPosition = Center,
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = {
                        if (state.displayState != DisplayState.DISPLAYING_RESULTS) {
                            onEvent(VoiceToTextEvent.ToggleRecording(languageCode))
                        } else {
                            onResult(state.spokenText)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(75.dp)
                ) {
                    AnimatedContent(targetState = state.displayState, label = "") { displayState ->
                        when (displayState) {
                            DisplayState.SPEAKING -> {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(id = R.string.stop_recording),
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            DisplayState.DISPLAYING_RESULTS -> {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = stringResource(id = R.string.apply),
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            else -> {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.mic),
                                    contentDescription = stringResource(id = R.string.record_audio),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                }
                if (state.displayState == DisplayState.DISPLAYING_RESULTS) {
                    IconButton(onClick = {
                        onEvent(VoiceToTextEvent.ToggleRecording(languageCode))
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(id = R.string.record_again),
                            tint = LightBlue
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
                        onEvent(VoiceToTextEvent.Close)
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
                if (state.displayState == DisplayState.SPEAKING) {
                    Text(
                        text = stringResource(id = R.string.listening),
                        color = LightBlue,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(targetState = state.displayState, label = "") { displayState ->
                    when (displayState) {
                        DisplayState.WAITING_TO_TALK -> {
                            Text(
                                text = stringResource(id = R.string.start_talking),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )
                        }

                        DisplayState.SPEAKING -> {
                            VoiceRecorderDisplay(
                                powerRatios = state.powerRatios,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                        }

                        DisplayState.DISPLAYING_RESULTS -> {
                            Text(
                                text = state.spokenText,
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )
                        }

                        DisplayState.ERROR -> {
                            Text(
                                text = state.recordError ?: "Unknown error",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceToTextBottomSheet(
    state: VoiceToTextState,
    languageCode: String,
    onResult: (String) -> Unit,
    onEvent: (VoiceToTextEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onEvent(
            VoiceToTextEvent.PermissionResult(
                isGranted,
                !isGranted && (context as ComponentActivity).shouldShowRequestPermissionRationale(
                    android.Manifest.permission.RECORD_AUDIO
                )
            )
        )
        if (isGranted) {
            onEvent(VoiceToTextEvent.ToggleRecording(languageCode))
        }
    }

    LaunchedEffect(recordAudioPermissionLauncher) {
        recordAudioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Text(
                text = "Say something",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            AnimatedContent(targetState = state.displayState, label = "") { displayState ->
                when (displayState) {
                    DisplayState.WAITING_TO_TALK,
                    DisplayState.SPEAKING -> {
                        VoiceRecorderDisplay(
                            powerRatios = state.powerRatios,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }

                    DisplayState.DISPLAYING_RESULTS -> {
                        Text(
                            text = state.spokenText,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    DisplayState.ERROR -> {
                        Text(
                            text = state.recordError ?: "Unknown error",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    else -> Unit
                }
            }
        }) {

    }
}

@Preview
@Composable
fun VoiceToTextPreview() {
    VoiceToTextBottomSheet(
        state = VoiceToTextState(displayState = DisplayState.SPEAKING, powerRatios = listOf(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f)),
        languageCode = "en-US",
        onResult = {},
        onEvent = {}
    )
}