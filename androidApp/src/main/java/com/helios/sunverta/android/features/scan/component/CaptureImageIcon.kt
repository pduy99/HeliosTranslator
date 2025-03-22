package com.helios.sunverta.android.features.scan.component

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CaptureImageIcon(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var pressDurationExceeded by remember { mutableStateOf(false) }

    // Reset the press effect after 300 ms
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(300)
            pressDurationExceeded = true
        }
    }

    Canvas(
        modifier = modifier
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isPressed = true
                        pressDurationExceeded = false
                        true
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if (!pressDurationExceeded) {
                            onClick()
                        }
                        isPressed = false
                        true
                    }

                    else -> false
                }
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        drawCircle(
            color = color,
            center = Offset(centerX, centerY),
            radius = canvasWidth / 2,
            style = Stroke(width = 5f)
        )

        val innerCircleColor =
            if (isPressed && !pressDurationExceeded) color.copy(alpha = 0.8f) else color

        drawCircle(
            color = innerCircleColor,
            center = Offset(centerX, centerY),
            radius = (canvasWidth / 2) * 0.9f,
        )
    }
}