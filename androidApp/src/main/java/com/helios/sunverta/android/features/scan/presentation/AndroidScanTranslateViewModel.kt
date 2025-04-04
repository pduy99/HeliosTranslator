package com.helios.sunverta.android.features.scan.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.resolutionselector.ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.sunverta.core.data.repository.LanguageRepository
import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.features.scantranslate.ScanTranslateEvent
import com.helios.sunverta.features.scantranslate.ScanTranslateViewModel
import com.helios.sunverta.features.scantranslate.domain.ImageTranslator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AndroidScanTranslateViewModel @Inject constructor(
    imageTranslator: ImageTranslator,
    languageRepository: LanguageRepository
) : ViewModel() {

    private val viewModel = ScanTranslateViewModel(
        imageTranslator,
        languageRepository,
        viewModelScope
    )

    val uiState = viewModel.state

    private var camera: Camera? = null

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
        }
    }

    private val cameraCaptureUseCase by lazy {
        val capturedImageWidth = 1920
        val capturedImageHeight = 1080

        val resolutionStrategy = ResolutionStrategy(
            Size(capturedImageWidth, capturedImageHeight),
            FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
        )

        val resolutionSelector = ResolutionSelector.Builder()
            .setResolutionStrategy(resolutionStrategy)
            .build()

        ImageCapture.Builder()
            .setResolutionSelector(resolutionSelector)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    suspend fun bindToCamera(appContext: Context, lifeCycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        val camera = processCameraProvider.bindToLifecycle(
            lifeCycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase,
            cameraCaptureUseCase
        )
        this.camera = camera

        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
            this.camera = null
        }
    }

    private fun toggleTorch() {
        camera?.cameraControl?.enableTorch(!uiState.value.isTorchEnable)
    }

    fun tapToFocus(tapCoords: Offset) {
        val point = surfaceMeteringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            camera?.cameraControl?.startFocusAndMetering(meteringAction)
        }
    }

    private suspend fun captureImage() = suspendCoroutine { continuation ->
        cameraCaptureUseCase.takePicture(
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    super.onCaptureSuccess(imageProxy)

                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    val bitmap = imageProxyToBitmap(imageProxy)

                    val rotatedBitmap = if (rotationDegrees != 0) {
                        val matrix = Matrix()
                        matrix.postRotate(rotationDegrees.toFloat())
                        Bitmap.createBitmap(
                            bitmap,
                            0, 0,
                            bitmap.width, bitmap.height,
                            matrix,
                            true
                        )
                    } else {
                        bitmap
                    }

                    imageProxy.close()
                    continuation.resume(CommonImage(rotatedBitmap, rotationDegrees))
                }

                override fun onError(exception: ImageCaptureException) {
                    continuation.resumeWithException(exception)
                }
            }
        )
    }

    private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
        val buffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun onEvent(event: ScanTranslateEvent) {
        when (event) {
            is ScanTranslateEvent.ToggleTorch -> {
                toggleTorch()
                viewModel.onEvent(event)
            }

            is ScanTranslateEvent.CaptureImage -> {
                viewModelScope.launch {
                    val capturedImage = captureImage()
                    onEvent(ScanTranslateEvent.TranslateImage(capturedImage))
                }
            }

            else -> {
                viewModel.onEvent(event)
            }
        }
    }

    fun cleanup() {
        _surfaceRequest.update {
            null
        }
        camera = null
    }
}