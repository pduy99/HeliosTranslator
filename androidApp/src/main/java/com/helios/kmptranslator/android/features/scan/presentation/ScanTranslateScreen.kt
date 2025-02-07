package com.helios.kmptranslator.android.features.scan.presentation

import android.Manifest
import android.graphics.Paint
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.TorchState
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.features.scan.component.CameraFunctionButtons
import com.helios.kmptranslator.android.features.scan.component.CameraPreviewScreenHeader
import com.helios.kmptranslator.android.features.scan.component.CameraTranslateHolder
import com.helios.kmptranslator.android.features.scan.component.CaptureImageIcon
import com.helios.kmptranslator.android.features.scan.component.TranslatedImageHeader
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.features.scantranslate.ScanTranslateEvent
import com.helios.kmptranslator.features.scantranslate.ScanTranslateUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanTranslateScreen(
    modifier: Modifier = Modifier,
    uiState: ScanTranslateUiState,
    surfaceRequest: SurfaceRequest?,
    onTapToFocus: (Offset) -> Unit,
    onEvent: (ScanTranslateEvent) -> Unit,
    onNavigateUp: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    ) { granted ->
        if (!granted) {
            onPermissionDenied.invoke()
        }
    }

    LaunchedEffect(key1 = Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    when {
        cameraPermissionState.status.isGranted -> {
            surfaceRequest?.let { surfaceRequest ->
                CameraTranslateHolder(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    fromLanguage = uiState.fromLanguage,
                    toLanguage = uiState.toLanguage
                ) {
                    if (uiState.capturedImage == null) {
                        CameraPreview(
                            modifier = Modifier.weight(1f),
                            surfaceRequest = surfaceRequest,
                            isTorchEnable = uiState.isTorchEnable,
                            onImageCapture = {
                                onEvent(ScanTranslateEvent.CaptureImage)
                            },
                            onToggleTorch = {
                                onEvent(ScanTranslateEvent.ToggleTorch)
                            },
                            onTapToFocus = onTapToFocus,
                            onNavigateUp = onNavigateUp
                        )
                    } else {
                        Box(
                            modifier = modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            TranslatedImage(
                                uiState = uiState,
                                onDiscardImage = {
                                    onEvent(ScanTranslateEvent.Reset)
                                }
                            )

                            if (uiState.isTranslating) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.inversePrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        cameraPermissionState.status is PermissionStatus.Denied -> {
            if ((cameraPermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                onPermissionDenied.invoke()
            }
        }
    }
}

@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    surfaceRequest: SurfaceRequest,
    isTorchEnable: Boolean,
    onTapToFocus: (Offset) -> Unit,
    onImageCapture: () -> Unit,
    onToggleTorch: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val inspectionMode = LocalInspectionMode.current
    val coordinateTransformer = remember { MutableCoordinateTransformer() }

    Box(modifier = modifier.fillMaxSize()) {
        if (!inspectionMode) {
            CameraXViewfinder(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapCoords ->
                            with(coordinateTransformer) {
                                onTapToFocus(tapCoords.transform())
                            }
                        }
                    },
                surfaceRequest = surfaceRequest,
                coordinateTransformer = coordinateTransformer
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }
        CameraPreviewScreenHeader(
            modifier = Modifier.align(Alignment.TopCenter),
            onNavigateUp = onNavigateUp
        )
        CameraFunctionButtons(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            torchState = if (isTorchEnable) TorchState.ON else TorchState.OFF,
            onOpenGalleryClick = {},
            onCaptureImageButtonClick = onImageCapture,
            onFlashButtonClick = onToggleTorch
        )
    }
}

@Composable
private fun BoxScope.TranslatedImage(
    uiState: ScanTranslateUiState,
    onDiscardImage: () -> Unit
) {
    var translatedTextVisible by remember {
        mutableStateOf(true)
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        bitmap = uiState.capturedImage!!.toImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    if (uiState.translatedTextBlock.isNotEmpty() && translatedTextVisible) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Get original image dimensions
            val originalWidth =
                uiState.capturedImage!!.bitmap.width.toFloat()
            val originalHeight =
                uiState.capturedImage!!.bitmap.height.toFloat()

            // Get displayed image dimensions
            val displayedWidth = size.width
            val displayedHeight = size.height

            // Scaling factors
            val scaleX = displayedWidth / originalWidth
            val scaleY = displayedHeight / originalHeight

            uiState.translatedTextBlock.forEach { block ->
                block.boundingBox?.let { rect ->
                    drawIntoCanvas { canvas ->
                        // Calculate scaled text size based on original text height
                        val originalTextHeight = rect.height.toFloat()
                        val scaledTextSize =
                            (originalTextHeight * scaleY * 0.8f).coerceAtMost(40f)


                        val textPaint = Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = scaledTextSize
                        }

                        val backgroundPaint =
                            Paint().apply {
                                color = android.graphics.Color.WHITE
                                style = Paint.Style.FILL
                            }

                        // Scale bounding box coordinates to match displayed image size
                        val left = rect.left * scaleX
                        val top = rect.top * scaleY

                        // Calculate text background size
                        val textWidth = textPaint.measureText(block.text)
                        val textHeight = textPaint.textSize
                        val padding = 8f

                        // Draw background rectangle
                        canvas.nativeCanvas.drawRect(
                            left - padding,
                            top - padding,
                            left + textWidth + padding,
                            top + textHeight + padding,
                            backgroundPaint
                        )

                        // Draw translated text on top
                        canvas.nativeCanvas.drawText(
                            block.text,
                            left,
                            top + textHeight,
                            textPaint
                        )
                    }
                }
            }
        }
    }

    if (!uiState.isTranslating) {
        TranslatedImageHeader(
            modifier = Modifier.align(Alignment.TopCenter),
            textVisible = translatedTextVisible,
            onClose = onDiscardImage,
            onToggleTextVisibility = {
                translatedTextVisible = !translatedTextVisible
            }
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraPreviewPreview() {
    HeliosTranslatorTheme {
        ScanTranslateScreen(
            modifier = Modifier.fillMaxSize(),
            uiState = ScanTranslateUiState(
                isTranslating = false,
                translatedTextBlock = emptyList(),
                fromLanguage = UiLanguage.byCode("en"),
                toLanguage = UiLanguage.byCode("ja")
            ),
            onNavigateUp = {},
            onEvent = {},
            surfaceRequest = null,
            onPermissionDenied = {},
            onTapToFocus = {}
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CaptureImageIconPreview() {
    CaptureImageIcon(
        modifier = Modifier.size(65.dp),
        color = Color.White,
        onClick = {}
    )
}

