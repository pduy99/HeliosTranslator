package com.helios.kmptranslator.android.voicetranslate.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.texttranslate.components.gradientSurface
import kotlin.random.Random

@Composable
fun VoiceRecorderDisplay(
    powerRatios: List<Float>,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .gradientSurface()
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .drawBehind {
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
                        .reversed()
                        .forEachIndexed { index, ratio ->
                            val yTopStart = center.y - (size.height / 2f) * ratio
                            drawRoundRect(
                                color = primary,
                                topLeft = Offset(
                                    x = size.width - index * 2 * powerRatiWidth,
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
fun VoiceRecorderDisplayPreview() {
    HeliosTranslatorTheme {
        VoiceRecorderDisplay(powerRatios = (0..50).map {
            Random.nextFloat()
        }, modifier = Modifier.fillMaxWidth().height(100.dp))
    }
}