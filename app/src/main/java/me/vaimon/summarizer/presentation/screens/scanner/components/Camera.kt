package me.vaimon.summarizer.presentation.screens.scanner.components

import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun Camera(
    zoom: Float,
    isCaptureRequired: Boolean,
    onCapture: (ImageProxy) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val localContext = LocalContext.current

    var camera by remember { mutableStateOf<Camera?>(null) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .build()
    }

    val preview = remember {
        Preview.Builder()
            .build()
    }

    val cameraSelector = remember {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context)
            preview.setSurfaceProvider(previewView.getSurfaceProvider())

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                camera = cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    imageCapture,
                    preview
                )
            }, ContextCompat.getMainExecutor(context))

            previewView
        },
        modifier = modifier
    )
    if (isCaptureRequired) {
        LaunchedEffect(key1 = imageCapture) {
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(localContext),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        onCapture(image)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            localContext,
                            "Capture failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    LaunchedEffect(key1 = zoom, key2 = camera) {
        camera?.cameraControl?.setLinearZoom(zoom)
    }
}