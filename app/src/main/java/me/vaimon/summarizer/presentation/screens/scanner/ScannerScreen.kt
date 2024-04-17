package me.vaimon.summarizer.presentation.screens.scanner

import androidx.camera.core.ImageProxy
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.navigation.NavigationDestination
import me.vaimon.summarizer.presentation.screens.components.PrimaryActionButton
import me.vaimon.summarizer.presentation.screens.home.HomeScreenDestination
import me.vaimon.summarizer.presentation.screens.scanner.components.BackButton
import me.vaimon.summarizer.presentation.screens.scanner.components.Camera
import me.vaimon.summarizer.presentation.screens.scanner.components.ScannedTextPreview
import me.vaimon.summarizer.presentation.screens.scanner.components.ScanningIndicator
import me.vaimon.summarizer.presentation.theme.SummarizerTheme
import me.vaimon.summarizer.util.swapAxes

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

    ScannerBody(
        uiState = uiState,
        scannedText = scannedText,
        navigateBack = navController::navigateUp,
        onBtnCaptureClick = viewModel::requireCapture,
        onBtnCopyClick = viewModel::onBtnCopyClick,
        onCapture = viewModel::onCapture
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannerBody(
    navigateBack: () -> Unit,
    uiState: ScannerViewModel.UiState,
    scannedText: String?,
    onBtnCaptureClick: () -> Unit,
    onBtnCopyClick: () -> Unit,
    onCapture: (ImageProxy) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiState.isCameraMode) {
            val sliderState = remember {
                SliderState(.5f)
            }
            Box(modifier = modifier.fillMaxSize()) {
                Camera(
                    zoom = sliderState.value,
                    isCaptureRequired = uiState.isCaptureRequired,
                    onCapture = onCapture,
                    modifier = Modifier.fillMaxSize()
                )

                Slider(
                    state = sliderState,
                    modifier = Modifier
                        .height(256.dp)
                        .swapAxes()
                        .align(Alignment.CenterEnd)
                )

                PrimaryActionButton(
                    icon = R.drawable.ic_scan,
                    onClick = onBtnCaptureClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }

        AnimatedVisibility(visible = !uiState.isCameraMode) {
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

        BackButton(
            isTranslucent = uiState.isCameraMode,
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
            ScannerBody(
                navigateBack = { /*TODO*/ },
                uiState = ScannerViewModel.UiState(),
                scannedText = "",
                onBtnCaptureClick = { /*TODO*/ },
                onBtnCopyClick = { /*TODO*/ },
                onCapture = {}
            )
        }
    }
}