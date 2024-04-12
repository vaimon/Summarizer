package me.vaimon.summarizer.presentation.screens.scanner

import androidx.camera.core.ImageProxy
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.navigation.NavigationDestination
import me.vaimon.summarizer.presentation.screens.components.PrimaryActionButton
import me.vaimon.summarizer.presentation.screens.home.HomeScreenDestination
import me.vaimon.summarizer.presentation.screens.scanner.components.Camera
import me.vaimon.summarizer.presentation.theme.SummarizerTheme
import me.vaimon.summarizer.presentation.theme.bodySecondaryMedium
import me.vaimon.summarizer.presentation.theme.onTranslucentBackground
import me.vaimon.summarizer.presentation.theme.translucentBackground

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
            Box(modifier = modifier.fillMaxSize()) {
                Camera(
                    isCaptureRequired = uiState.isCaptureRequired,
                    onCapture = onCapture,
                    modifier = Modifier.fillMaxSize()
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

@Composable
fun ScannedTextPreview(
    scannedText: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        scannedText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
            )
        } ?: run {
            Text(
                text = stringResource(R.string.desc_error_scan),
                style = MaterialTheme.typography.bodySecondaryMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ScanningIndicator(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_scanning))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

@Composable
fun BackButton(
    isTranslucent: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (isTranslucent)
                MaterialTheme.colorScheme.translucentBackground
            else
                MaterialTheme.colorScheme.background,
            contentColor = if (isTranslucent)
                MaterialTheme.colorScheme.onTranslucentBackground
            else
                MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
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