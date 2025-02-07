package com.helios.sunverta.android.features.conversation.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.sunverta.android.R
import com.helios.sunverta.android.core.components.LanguageDropDown
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.core.presentation.UiLanguage

@Composable
fun VoiceTranslateBox(
    modifier: Modifier = Modifier,
    voiceAnimationState: VoiceState,
    speakingLanguage: UiLanguage,
    content: String,
    isMirrored: Boolean = false,
    onIdleClick: () -> Unit,
    onActiveClick: () -> Unit,
    isChoosingLanguage: Boolean,
    onLanguageDropdownClick: () -> Unit,
    onLanguageDropDownDismiss: () -> Unit,
    onSelectLanguage: (UiLanguage) -> Unit,
) {
    var showHint by remember {
        mutableStateOf(true)
    }

    val rotation by animateFloatAsState(
        targetValue = if (isMirrored) 180f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "mirroredAnimation"
    )

    LaunchedEffect(content, voiceAnimationState) {
        if (content.isNotBlank() || voiceAnimationState !is VoiceState.Idle) {
            showHint = false
        }
    }

    Column(
        modifier = modifier
            .graphicsLayer {
                rotationY = if (isMirrored) 180f else 0f
                rotationX = rotation
            }
            .clip(RoundedCornerShape(26.dp))
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp)
    ) {
        LanguageDropDown(
            language = speakingLanguage,
            isOpen = isChoosingLanguage,
            onClick = onLanguageDropdownClick,
            onDismiss = onLanguageDropDownDismiss,
            onSelectLanguage = onSelectLanguage,
            modifier = Modifier
                .padding(16.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = if (showHint) stringResource(id = R.string.voice_translate_hint) else content,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.speaker),
                    contentDescription = stringResource(id = R.string.play_loud),
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimationPlayStopButton(
                modifier = Modifier.size(80.dp),
                state = voiceAnimationState,
                onActiveClick = onActiveClick,
                onIdleClick = onIdleClick
            )
        }
    }
}

@Preview
@Composable
private fun A1() {
    HeliosTranslatorTheme(darkTheme = true) {
        VoiceTranslateBox(
            speakingLanguage = UiLanguage.byCode("en"),
            voiceAnimationState = VoiceState.Idle,
            content = "Tap the mic button to start the conversation",
            onIdleClick = {},
            onActiveClick = {},
            isChoosingLanguage = false,
            onLanguageDropdownClick = {},
            onLanguageDropDownDismiss = {},
            onSelectLanguage = {}
        )
    }
}

@Preview
@Composable
private fun A2() {
    HeliosTranslatorTheme(darkTheme = true) {
        VoiceTranslateBox(
            speakingLanguage = UiLanguage.byCode("en"),
            voiceAnimationState = VoiceState.Active(0.8f),
            content = "Tap the mic button to start the conversation",
            onIdleClick = {},
            onActiveClick = {},
            isChoosingLanguage = false,
            onLanguageDropdownClick = {},
            onLanguageDropDownDismiss = {},
            onSelectLanguage = {}
        )
    }
}

@Preview
@Composable
private fun A3() {
    HeliosTranslatorTheme(darkTheme = true) {
        VoiceTranslateBox(
            speakingLanguage = UiLanguage.byCode("en"),
            voiceAnimationState = VoiceState.Idle,
            content = "Tap the mic button to start the conversation",
            isMirrored = true,
            onIdleClick = {},
            onActiveClick = {},
            isChoosingLanguage = false,
            onLanguageDropdownClick = {},
            onLanguageDropDownDismiss = {},
            onSelectLanguage = {}
        )
    }
}