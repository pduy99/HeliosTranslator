package com.helios.kmptranslator.android.features.scan.presentation

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.WindowManager
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.resolutionselector.ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.android.features.scan.component.CameraFunctionButtons
import com.helios.kmptranslator.android.features.scan.component.CameraPreviewScreenHeader
import com.helios.kmptranslator.android.features.scan.component.CameraTranslateHolder
import com.helios.kmptranslator.android.features.scan.component.CaptureImageIcon
import com.helios.kmptranslator.core.domain.model.CommonImage
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.features.scantranslate.ScanTranslateEvent
import com.helios.kmptranslator.features.scantranslate.ScanTranslateUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanTranslateScreen(
    modifier: Modifier = Modifier,
    uiState: ScanTranslateUiState,
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
                        onImageCapture = {
                            onEvent(ScanTranslateEvent.CaptureImage(CommonImage(it)))
                        },
                        onNavigateUp = onNavigateUp
                    )
                } else {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            bitmap = uiState.capturedImage!!.toImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )

                        if (uiState.translatedTextBlock.isNotEmpty()) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Get original image dimensions
                                val originalWidth = uiState.capturedImage!!.bitmap.width.toFloat()
                                val originalHeight = uiState.capturedImage!!.bitmap.height.toFloat()

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
                                            val scaledTextSize = originalTextHeight * scaleY * 0.8f // Adjust factor for better fit


                                            val paint = android.graphics.Paint().apply {
                                                color = android.graphics.Color.YELLOW
                                                textSize = scaledTextSize
                                            }

                                            val backgroundPaint = android.graphics.Paint().apply {
                                                color = android.graphics.Color.argb(
                                                    150,
                                                    0,
                                                    0,
                                                    0
                                                ) // Semi-transparent black
                                                style = android.graphics.Paint.Style.FILL
                                            }

                                            // Scale bounding box coordinates to match displayed image size
                                            val left = rect.left * scaleX
                                            val top = rect.top * scaleY

                                            // Calculate text background size
                                            val textWidth = paint.measureText(block.text)
                                            val textHeight = paint.textSize
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
                                                paint
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (uiState.isTranslating) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
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
    onImageCapture: (Bitmap) -> Unit,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    var camera by remember {
        mutableStateOf<Camera?>(null)
    }

    val torchState = camera?.cameraInfo?.torchState?.observeAsState()?.value ?: TorchState.OFF

    val capturedImageWidth = remember {
        1920
    }
    val capturedImageHeight = remember {
        1080
    }

    val resolutionStrategy = ResolutionStrategy(
        Size(capturedImageWidth, capturedImageHeight),
        FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
    )

    val resolutionSelector = ResolutionSelector.Builder()
        .setResolutionStrategy(resolutionStrategy)
        .build()

    val imageCapture = remember {
        ImageCapture.Builder()
            .setResolutionSelector(resolutionSelector)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

    }
    val inspectionMode = LocalInspectionMode.current

    Box(modifier = modifier.fillMaxSize()) {
        if (!inspectionMode) {
            CameraPreviewCompose(
                modifier = Modifier.fillMaxSize(),
                imageCapture = imageCapture,
                onCameraChanged = {
                camera = it
            })
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
            torchState = torchState,
            onOpenGalleryClick = {},
            onCaptureImageButtonClick = {
                captureImage(imageCapture, context, onImageCapture)
            },
            onFlashButtonClick = {
                camera?.cameraControl?.enableTorch(torchState != TorchState.ON)
            }
        )
    }
}

@Composable
fun Builder() {
    TODO("Not yet implemented")
}

@Composable
private fun CameraPreviewCompose(
    modifier: Modifier = Modifier,
    imageCapture: ImageCapture,
    onCameraChanged: (Camera?) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    var camera by remember {
        mutableStateOf<Camera?>(null)
    }

    // Add a state to track if the camera is bound
    var isCameraBound by remember { mutableStateOf(false) }

    LaunchedEffect(camera) {
        onCameraChanged(camera)
    }

    fun bindCamera(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
        )

        preview.surfaceProvider = previewView.surfaceProvider
        isCameraBound = true
    }

    // Function to unbind the camera use cases
    fun unbindCamera(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
        camera = null
        isCameraBound = false
    }

    DisposableEffect(lifecycleOwner) {
        val cameraProvider = cameraProviderFuture.get()

        onDispose {
            if (isCameraBound) {
                unbindCamera(cameraProvider)
            }
        }
    }

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        bindCamera(cameraProvider)
    }

    AndroidView({ previewView }, modifier = modifier)
}

private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    onImageCapture: (Bitmap) -> Unit
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                super.onCaptureSuccess(imageProxy)

                val bitmap = imageProxy.toBitmap()

                // Get device rotation
                val rotation = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                    .defaultDisplay.rotation

                // Determine the rotation angle
                val rotationDegrees = when (rotation) {
                    Surface.ROTATION_0 -> 0
                    Surface.ROTATION_90 -> 90
                    Surface.ROTATION_180 -> 180
                    Surface.ROTATION_270 -> 270
                    else -> 0
                }

                // Rotate the bitmap
                val rotatedBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    Matrix().apply { postRotate(90f) },
                    true
                )

                Log.d("CameraCapture", "Preview Rotation: $rotationDegrees")
                Log.d(
                    "CameraCapture",
                    "Captured Bitmap Size: ${rotatedBitmap.width}x${rotatedBitmap.height}"
                )

                imageProxy.close()
                onImageCapture(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraCapture", "Image capture failed", exception)
            }
        }
    )
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
            onPermissionDenied = {}
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

