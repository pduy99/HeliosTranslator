package com.helios.kmptranslator.android.features.scan.presentation

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.helios.kmptranslator.android.core.components.LanguagePickerComponent
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.core.presentation.UiLanguage
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(
    modifier: Modifier = Modifier,
    onImageCapture: (Bitmap) -> Unit,
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
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                CameraPreviewContent(
                    modifier = Modifier.weight(1f),
                    onImageCapture = onImageCapture,
                    onNavigateUp = onNavigateUp
                )
                LanguagePickerComponent(
                    fromLanguage = UiLanguage.byCode("en"),
                    isChoosingFromLanguage = false,
                    toLanguage = UiLanguage.byCode("ja"),
                    isChoosingToLanguage = false,
                    onEvent = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .safeContentPadding()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
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
private fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    onImageCapture: (Bitmap) -> Unit,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    var camera by remember {
        mutableStateOf<Camera?>(null)
    }

    val torchState = camera?.cameraInfo?.torchState?.observeAsState()?.value ?: TorchState.OFF

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
    }

    val inspectionMode = LocalInspectionMode.current

    Box(modifier = modifier.fillMaxSize()) {
        if (!inspectionMode) {
            CameraPreviewCompose(modifier = Modifier.fillMaxSize(), onCameraChanged = {
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
        CameraFunctionButtons(modifier = Modifier.align(alignment = Alignment.BottomCenter),
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
private fun CameraPreviewCompose(
    modifier: Modifier = Modifier,
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

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
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

@Composable
private fun CameraPreviewScreenHeader(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            onClick = onNavigateUp
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f))
                .padding(8.dp)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Take a picture to translate text",
                style = TextStyle(color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
            )
        }
    }
}

@Composable
private fun CameraFunctionButtons(
    modifier: Modifier = Modifier,
    torchState: Int,
    onCaptureImageButtonClick: () -> Unit,
    onOpenGalleryClick: () -> Unit,
    onFlashButtonClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceContainer),
            onClick = onOpenGalleryClick
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        CaptureImageIcon(
            modifier = Modifier.size(65.dp),
            color = Color.White,
            onClick = onCaptureImageButtonClick
        )

        IconButton(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceContainer),
            onClick = onFlashButtonClick
        ) {
            Icon(
                imageVector = if (torchState == TorchState.ON) Icons.Outlined.FlashOff else Icons.Outlined.FlashOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
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
                imageProxy.close() // Don't forget to close the imageProxy
                onImageCapture(bitmap)
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
        CameraPreviewScreen(
            modifier = Modifier.fillMaxSize(),
            onImageCapture = {},
            onNavigateUp = {},
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

