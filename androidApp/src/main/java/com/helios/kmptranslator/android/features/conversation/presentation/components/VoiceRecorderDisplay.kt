package com.helios.kmptranslator.android.features.conversation.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import kotlin.random.Random

@Composable
fun VoiceRecorderDisplay(
    powerRatios: List<Float>,
    modifier: Modifier = Modifier,
) {
    val onSurfaceContainer = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = modifier
        .padding(start = 0.dp, top = 24.dp, bottom = 24.dp, end = 16.dp),
        onDraw = {
            val powerRatiWidth = 3.dp.toPx()
            val powerRatioCount = (size.width / (2 * powerRatiWidth)).toInt()

            clipRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height
            ) {
                powerRatios
                    .takeLast(powerRatioCount)
                    .forEachIndexed { index, ratio ->
                        val yTopStart = center.y - (size.height / 2f) * ratio
                        drawRoundRect(
                            color = onSurfaceContainer,
                            topLeft = Offset(
                                x = index * 2 * powerRatiWidth,
                                y = yTopStart
                            ),
                            size = Size(
                                width = powerRatiWidth,
                                height = (center.y - yTopStart) * 2f
                            ),
                            cornerRadius = CornerRadius(100f)
                        )
                    }
            }
        })
}


@Preview
@Composable
private fun Preview() {
    HeliosTranslatorTheme(darkTheme = true) {
        VoiceRecorderDisplay(
            powerRatios = (0..90).map {
                Random.nextFloat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )
    }
}