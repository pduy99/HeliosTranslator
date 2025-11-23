package com.helios.sunverta.android.features.conversation.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme

private const val BUTTON_SIZE_RATIO = 0.7f
private const val INNER_ORB_RATIO = 1.3f
private const val MID_ORB_RADIO = 1.6f
private const val OUTER_ORB_RADIO = 1.9f

sealed interface VoiceState {
    data object Idle : VoiceState
    data class Active(val volume: Float) : VoiceState
}

@Composable
fun AnimationPlayStopButton(
    modifier: Modifier,
    state: VoiceState,
    color: Color = MaterialTheme.colorScheme.inversePrimary,
    onIdleClick: () -> Unit,
    onActiveClick: () -> Unit
) {
    when (state) {
        is VoiceState.Idle -> IdleVoiceAnimation(
            modifier = modifier,
            color = color,
            onIdleClick
        )

        is VoiceState.Active -> ActiveVoiceAnimation(
            modifier = modifier,
            state.volume,
            color = color,
            onActiveClick
        )
    }

}

@Composable
private fun IdleVoiceAnimation(
    modifier: Modifier = Modifier,
    color: Color,
    onIdleClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxSize(BUTTON_SIZE_RATIO)
                .align(Alignment.Center),
            onClick = onIdleClick,
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(1.dp, color)
        ) {
            Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = null,
                tint = color
            )
        }
    }
}


@Composable
private fun ActiveVoiceAnimation(
    modifier: Modifier = Modifier,
    volume: Float,
    color: Color,
    onActiveClick: () -> Unit
) {
    val scale by animateFloatAsState(
        volume.coerceIn(0f, 1f),
        animationSpec = tween(200), label = "VoicePulseAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val baseRadius = size.minDimension / 2f * BUTTON_SIZE_RATIO
            val innerRadius =
                baseRadius + (size.minDimension / 2f * INNER_ORB_RATIO - baseRadius) * scale
            val midRadius =
                baseRadius + (size.minDimension / 2f * MID_ORB_RADIO - baseRadius) * scale
            val outerRadius =
                baseRadius + (size.minDimension / 2f * OUTER_ORB_RADIO - baseRadius) * scale

            drawCircle(Color(0xFF2196F3).copy(alpha = 0.32f), radius = innerRadius, center = center)
            drawCircle(color.copy(alpha = 0.16f), radius = midRadius, center = center)
            drawCircle(color.copy(alpha = 0.08f), radius = outerRadius, center = center)
        }

        IconButton(
            onClick = {
                onActiveClick.invoke()
            },
            modifier = Modifier
                .fillMaxSize(BUTTON_SIZE_RATIO)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(color)
        ) {
            Icon(
                imageVector = Icons.Filled.Stop,
                contentDescription = "Stop",
                tint = Color.White,
                modifier = Modifier
                    .fillMaxSize(BUTTON_SIZE_RATIO / 2f)
            )
        }
    }
}

@Preview
@Composable
private fun A1() {
    HeliosTranslatorTheme {
        AnimationPlayStopButton(
            modifier = Modifier.size(80.dp),
            state = VoiceState.Active(0.5f),
            onActiveClick = {},
            onIdleClick = {}
        )
    }
}

@Preview
@Composable
private fun A3() {
    HeliosTranslatorTheme {
        AnimationPlayStopButton(
            modifier = Modifier.size(80.dp),
            state = VoiceState.Active(1f),
            onActiveClick = {},
            onIdleClick = {}
        )
    }
}

@Preview
@Composable
private fun A2() {
    HeliosTranslatorTheme {
        AnimationPlayStopButton(
            modifier = Modifier.size(80.dp),
            state = VoiceState.Idle,
            onActiveClick = {},
            onIdleClick = {}
        )
    }
}