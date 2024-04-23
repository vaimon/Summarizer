package me.vaimon.summarizer.presentation.screens.scanner

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.models.BoundingBox
import me.vaimon.summarizer.presentation.navigation.NavigationDestination
import me.vaimon.summarizer.presentation.screens.components.PrimaryActionButton
import me.vaimon.summarizer.presentation.screens.home.HomeScreenDestination
import me.vaimon.summarizer.presentation.screens.scanner.ScannerViewModel.ScannerMode
import me.vaimon.summarizer.presentation.screens.scanner.ScannerViewModel.UiState
import me.vaimon.summarizer.presentation.screens.scanner.components.CameraInterface
import me.vaimon.summarizer.presentation.screens.scanner.components.CroppingImage
import me.vaimon.summarizer.presentation.screens.scanner.components.ScannedTextPreview
import me.vaimon.summarizer.presentation.screens.scanner.components.ScanningIndicator
import me.vaimon.summarizer.presentation.screens.scanner.components.TranslucentButton
import me.vaimon.summarizer.presentation.theme.SummarizerTheme

object ScannerDestination : NavigationDestination {
    override val route = "scanner"
}

@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scannedText by viewModel.scannedText.collectAsState()
    val capturedImage by viewModel.capturedImage.collectAsState()

    ScannerBody(
        uiState = uiState,
        scannedText = scannedText,
        capturedImage = capturedImage,
        boundingBoxFlow = viewModel.boundingBox,
        navigateBack = navController::navigateUp,
        onBtnCaptureClick = viewModel::requireCapture,
        onBtnCopyClick = viewModel::onBtnCopyClick,
        onBtnScanClick = viewModel::onBtnScanClick,
        onCapture = viewModel::onCapture,
        onBoundingBoxChanged = viewModel::onBoundingBoxChanged
    )

    LaunchedEffect(key1 = uiState) {
        uiState.backNavigationArg?.let {
            viewModel.onNavigateBackHandled()
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(HomeScreenDestination.SCAN_RESULT_KEY, it)
            navController.popBackStack()
        }
    }
}

@Composable
private fun ScannerBody(
    navigateBack: () -> Unit,
    uiState: UiState,
    scannedText: String?,
    capturedImage: Bitmap?,
    boundingBoxFlow: StateFlow<BoundingBox?>,
    onBtnCaptureClick: () -> Unit,
    onBtnCopyClick: () -> Unit,
    onBtnScanClick: () -> Unit,
    onBoundingBoxChanged: (BoundingBox) -> Unit,
    onCapture: (ImageProxy) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiState.scannerMode == ScannerMode.Capture) {
            CameraInterface(
                uiState = uiState,
                onBtnCaptureClick = onBtnCaptureClick,
                onCapture = onCapture
            )
        }

        AnimatedVisibility(visible = uiState.scannerMode == ScannerMode.Crop) {
            Box(modifier = modifier.fillMaxSize()) {
                capturedImage?.let {
                    CroppingImage(
                        bitmap = it.asImageBitmap(),
                        boundingBoxFlow = boundingBoxFlow,
                        onBoundingBoxChanged = onBoundingBoxChanged,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                PrimaryActionButton(
                    icon = R.drawable.ic_forward,
                    onClick = onBtnScanClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }

        AnimatedVisibility(visible = uiState.scannerMode == ScannerMode.Preview) {
            if (uiState.isProcessing) {
                ScanningIndicator()
            } else {
                Box(modifier = modifier.fillMaxSize()) {
                    ScannedTextPreview(scannedText)

                    PrimaryActionButton(
                        icon = R.drawable.ic_copy,
                        onClick = onBtnCopyClick,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    )
                }
            }
        }

        TranslucentButton(
            iconPainter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
            isTranslucent = uiState.scannerMode != ScannerMode.Preview,
            onClick = navigateBack,
            modifier = Modifier
                .safeDrawingPadding()
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
fun ScannerPreview(
    modifier: Modifier = Modifier
) {
    SummarizerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

        }
    }
}