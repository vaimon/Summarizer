package me.vaimon.summarizer.presentation.screens.scanner.components

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.screens.components.PrimaryActionButton
import me.vaimon.summarizer.presentation.screens.scanner.ScannerViewModel
import me.vaimon.summarizer.util.swapAxes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraInterface(
    uiState: ScannerViewModel.UiState,
    onBtnCaptureClick: () -> Unit,
    onCapture: (ImageProxy) -> Unit,
    modifier: Modifier = Modifier
) {
    val sliderState = remember { SliderState(.5f) }
    var flashMode by remember { mutableIntStateOf(ImageCapture.FLASH_MODE_AUTO) }

    Box(modifier = modifier.fillMaxSize()) {
        Camera(
            zoom = sliderState.value,
            isCaptureRequired = uiState.isCaptureRequired,
            onCapture = onCapture,
            flashMode = flashMode,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            Modifier.align(Alignment.CenterEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Slider(
                state = sliderState,
                modifier = Modifier
                    .height(256.dp)
                    .swapAxes()
            )
            TranslucentButton(
                iconPainter = painterResource(
                    id = when (flashMode) {
                        ImageCapture.FLASH_MODE_AUTO -> R.drawable.ic_flash_auto
                        ImageCapture.FLASH_MODE_OFF -> R.drawable.ic_flash_off
                        ImageCapture.FLASH_MODE_ON -> R.drawable.ic_flash_on
                        else -> throw IllegalStateException()
                    }
                ),
                isTranslucent = true,
                onClick = {
                    flashMode = (flashMode + 1) % 3
                },
                modifier = Modifier
                    .safeDrawingPadding()
                    .padding(8.dp)
            )
        }

        PrimaryActionButton(
            icon = R.drawable.ic_camera,
            onClick = onBtnCaptureClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}