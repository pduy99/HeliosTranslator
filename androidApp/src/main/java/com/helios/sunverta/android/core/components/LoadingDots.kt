package com.helios.sunverta.android.core.components

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun Dot(
    infiniteTransition: InfiniteTransition,
    offset: Float,
    dotSize: Dp,
    delayUnit: Int,
    dotColor: Color
) {
    val scale = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at offset.toInt() using LinearEasing
                1f at (offset + delayUnit).toInt() using LinearEasing
                0f at (offset + delayUnit * 2).toInt()
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "dotScale"
    )

    Box(
        modifier = Modifier
            .size(dotSize)
            .scale(1f - scale.value)
            .background(dotColor, CircleShape)
    )
}

@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    dotSize: Dp = 12.dp,
    delayUnit: Int = 300,
    dotColor: Color = MaterialTheme.colorScheme.primary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loadingAnimation")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Dot(infiniteTransition, 0f, dotSize, delayUnit, dotColor)
        Spacer(modifier = Modifier.width(dotSize / 2))
        Dot(infiniteTransition, delayUnit.toFloat(), dotSize, delayUnit, dotColor)
        Spacer(modifier = Modifier.width(dotSize / 2))
        Dot(infiniteTransition, (delayUnit * 2).toFloat(), dotSize, delayUnit, dotColor)
    }
}