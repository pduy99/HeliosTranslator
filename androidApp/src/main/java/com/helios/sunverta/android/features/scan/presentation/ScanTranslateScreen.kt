package com.helios.sunverta.android.features.scan.presentation

import android.Manifest
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.TorchState
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.android.features.scan.component.CameraFunctionButtons
import com.helios.sunverta.android.features.scan.component.CameraPreviewScreenHeader
import com.helios.sunverta.android.features.scan.component.CameraTranslateHolder
import com.helios.sunverta.android.features.scan.component.CaptureImageIcon
import com.helios.sunverta.android.features.scan.component.TranslatedImageHeader
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.features.scantranslate.ScanTranslateEvent
import com.helios.sunverta.features.scantranslate.ScanTranslateUiState

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
    CameraPermissionHandler(
        onPermissionDenied = onPermissionDenied,
        content = { isPermissionGranted ->
            if (isPermissionGranted && surfaceRequest != null) {
                ScanTranslateContent(
                    modifier = modifier,
                    uiState = uiState,
                    surfaceRequest = surfaceRequest,
                    onTapToFocus = onTapToFocus,
                    onEvent = onEvent,
                    onNavigateUp = onNavigateUp
                )
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CameraPermissionHandler(
    onPermissionDenied: () -> Unit,
    content: @Composable (Boolean) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (!granted) onPermissionDenied()
    }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    when {
        cameraPermissionState.status.isGranted -> content(true)
        cameraPermissionState.status is PermissionStatus.Denied -> {
            if ((cameraPermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                onPermissionDenied()
            }
            content(false)
        }
    }
}

@Composable
private fun ScanTranslateContent(
    modifier: Modifier,
    uiState: ScanTranslateUiState,
    surfaceRequest: SurfaceRequest,
    onTapToFocus: (Offset) -> Unit,
    onEvent: (ScanTranslateEvent) -> Unit,
    onNavigateUp: () -> Unit,
) {
    CameraTranslateHolder(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding(),
        fromLanguage = uiState.fromLanguage,
        toLanguage = uiState.toLanguage
    ) {
        val shouldShowCamera = remember(uiState.capturedImage) { uiState.capturedImage == null }

        if (shouldShowCamera) {
            CameraPreview(
                modifier = Modifier.weight(1f),
                surfaceRequest = surfaceRequest,
                isTorchEnable = uiState.isTorchEnable,
                onImageCapture = { onEvent(ScanTranslateEvent.CaptureImage) },
                onToggleTorch = { onEvent(ScanTranslateEvent.ToggleTorch) },
                onTapToFocus = onTapToFocus,
                onNavigateUp = onNavigateUp
            )
        } else {
            TranslatedImageContent(
                modifier = Modifier.weight(1f),
                uiState = uiState,
                onDiscardImage = { onEvent(ScanTranslateEvent.Reset) }
            )
        }
    }
}

@Composable
private fun TranslatedImageContent(
    modifier: Modifier,
    uiState: ScanTranslateUiState,
    onDiscardImage: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        TranslatedImage(
            uiState = uiState,
            onDiscardImage = onDiscardImage
        )

        if (uiState.isTranslating) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.inversePrimary
            )
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

    val textAlpha by animateFloatAsState(
        targetValue = if (translatedTextVisible) 1f else 0f
    )

    Image(
        modifier = Modifier.fillMaxSize(),
        bitmap = uiState.capturedImage!!.toImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val originalWidth =
            uiState.capturedImage!!.bitmap.width.toFloat()
        val originalHeight =
            uiState.capturedImage!!.bitmap.height.toFloat()

        val displayedWidth = size.width
        val displayedHeight = size.height

        val scaleX = displayedWidth / originalWidth
        val scaleY = displayedHeight / originalHeight

        val backgroundPaint =
            Paint().apply {
                color = android.graphics.Color.WHITE
                style = Paint.Style.FILL
            }

        val textPaint = Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.LEFT
        }

        uiState.translatedTextBlock.forEach { block ->
            block.boundingBox?.let { rect ->
                drawIntoCanvas { canvas ->

                    backgroundPaint.alpha = (255 * textAlpha).toInt()
                    textPaint.alpha = (255 * textAlpha).toInt()

                    val rect = RectF(
                        rect.left * scaleX,
                        rect.top * scaleY,
                        rect.right * scaleX,
                        rect.bottom * scaleY
                    )

                    // Draw background rectangle
                    canvas.nativeCanvas.drawRect(
                        rect,
                        backgroundPaint
                    )

                    // Calculate optimal text size to fit the width and height
                    var textSize = 1f
                    val maxWidth = rect.width()
                    val maxHeight = rect.height()
                    val bounds = Rect()

                    // Binary search for the ideal text size
                    var low = 1f
                    var high = maxHeight
                    while (low < high) {
                        val mid = (low + high + 1) / 2
                        textPaint.textSize = mid
                        textPaint.getTextBounds(block.text, 0, block.text.length, bounds)

                        if (bounds.width() <= maxWidth && bounds.height() <= maxHeight) {
                            textSize = mid
                            low = mid
                        } else {
                            high = mid - 1
                        }
                    }

                    // Apply the found text size
                    textPaint.textSize = textSize

                    textPaint.getTextBounds(block.text, 0, block.text.length, bounds)

                    val x = rect.left + (maxWidth - bounds.width()) / 2
                    val y = rect.top + (maxHeight + bounds.height()) / 2

                    // Draw the translated text
                    canvas.nativeCanvas.drawText(block.text, x, y, textPaint)
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

@Preview
@Composable
fun CameraPreviewPreview() {
    HeliosTranslatorTheme {
        ScanTranslateScreen(
            modifier = Modifier.fillMaxSize(),
            uiState = ScanTranslateUiState(
                isTranslating = false,
                translatedTextBlock = emptyList(),
                fromLanguage = UiLanguage.fromLanguageCode("en"),
                toLanguage = UiLanguage.fromLanguageCode("ja")
            ),
            onNavigateUp = {},
            onEvent = {},
            surfaceRequest = null,
            onPermissionDenied = {},
            onTapToFocus = {}
        )
    }
}

@Preview
@Composable
fun CaptureImageIconPreview() {
    CaptureImageIcon(
        modifier = Modifier.size(65.dp),
        color = Color.White,
        onClick = {}
    )
}

