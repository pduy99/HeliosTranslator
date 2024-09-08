package com.helios.kmptranslator.android.voicetranslate.presentation.components

import VoiceAnimation
import VoiceAnimationState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.R
import com.helios.kmptranslator.android.core.components.LanguageDropDown
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.core.theme.LightBlue
import com.helios.kmptranslator.core.presentation.UiLanguage

@Composable
fun VoiceTranslateBox(
    modifier: Modifier = Modifier,
    voiceAnimationState: VoiceAnimationState,
    speakingLanguage: UiLanguage,
    onMicClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(horizontal = 16.dp)
    ) {
        LanguageDropDown(
            language = speakingLanguage,
            isOpen = false,
            onClick = { },
            onDismiss = { },
            onSelectLanguage = {},
            modifier = Modifier.padding(16.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = "Tap the mic button to start the conversation",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.speaker),
                    contentDescription = stringResource(id = R.string.play_loud),
                    tint = LightBlue
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.size(100.dp)) {
                VoiceAnimation(state = voiceAnimationState)
            }
        }
    }
}

@Preview
@Composable
private fun VoiceTranslateBoxPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        VoiceTranslateBox(
            speakingLanguage = UiLanguage.byCode("en"),
            voiceAnimationState = VoiceAnimationState.Idle,
            onMicClick = {}
        )
    }
}