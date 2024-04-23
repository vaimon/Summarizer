package me.vaimon.summarizer.presentation.screens.scanner

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtMost
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
import me.vaimon.summarizer.presentation.screens.scanner.components.ScannedTextPreview
import me.vaimon.summarizer.presentation.screens.scanner.components.ScanningIndicator
import me.vaimon.summarizer.presentation.screens.scanner.components.TranslucentButton
import me.vaimon.summarizer.presentation.theme.SummarizerTheme
import me.vaimon.summarizer.util.PointDp
import me.vaimon.summarizer.util.toDp

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

@Composable
fun CroppingImage(
    boundingBoxFlow: StateFlow<BoundingBox?>,
    bitmap: ImageBitmap,
    onBoundingBoxChanged: (BoundingBox) -> Unit,
    modifier: Modifier = Modifier
) {
    val boundingBox by boundingBoxFlow.collectAsState()

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .fillMaxSize()
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        CropRect(
            boundingBox = boundingBox,
            onRectChanged = { oldBoundingBox, topLeftOffset, widthOffset, heightOffset ->
                onBoundingBoxChanged(
                    BoundingBox(
                        width = (oldBoundingBox.width + widthOffset.toDp()).coerceAtMost(maxWidth),
                        height = (oldBoundingBox.height + heightOffset.toDp()).coerceAtMost(
                            maxHeight
                        ),
                        topLeftCorner = PointDp(
                            (oldBoundingBox.topLeftCorner.x + topLeftOffset.x.toDp()).coerceIn(0.dp..(maxWidth - oldBoundingBox.width)),
                            (oldBoundingBox.topLeftCorner.y + topLeftOffset.y.toDp()).coerceIn(0.dp..(maxHeight - oldBoundingBox.height))
                        )
                    )
                )
            }
        )


        LaunchedEffect(Unit) {
            onBoundingBoxChanged(
                BoundingBox(
                    PointDp(
                        maxWidth * 0.05f,
                        maxHeight * 0.05f
                    ),
                    maxWidth * 0.9f,
                    maxHeight * 0.9f
                )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CropRect(
    boundingBox: BoundingBox?,
    onRectChanged: (BoundingBox, Offset, Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    if (boundingBox == null) {
        return
    }

    val draggableState =
        rememberDraggable2DState { onRectChanged(boundingBox, it, 0f, 0f) }

    Box(
        modifier
            .sizeIn(minWidth = 32.dp, minHeight = 32.dp)
            .size(boundingBox.width, boundingBox.height)
            .offset(boundingBox.topLeftCorner.x, boundingBox.topLeftCorner.y)
            .border(BorderStroke(2.dp, color))
            .draggable2D(
                draggableState,
                startDragImmediately = true
            )
    ) {
        DragCorner(
            onDragged = { onRectChanged(boundingBox, it, -it.x, -it.y) },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(-8.dp, -8.dp)
        )
        DragCorner(
            onDragged = { onRectChanged(boundingBox, Offset(0f, it.y), it.x, -it.y) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(8.dp, -8.dp)
        )
        DragCorner(
            onDragged = { onRectChanged(boundingBox, Offset(it.x, 0f), -it.x, it.y) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(-8.dp, 8.dp)
        )
        DragCorner(
            onDragged = { onRectChanged(boundingBox, Offset(0f, 0f), it.x, it.y) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(8.dp, 8.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragCorner(
    modifier: Modifier = Modifier,
    onDragged: (Offset) -> Unit,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val draggableState = rememberDraggable2DState { onDragged(it) }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color)
            .size(16.dp)
            .draggable2D(
                draggableState,
                startDragImmediately = true
            )
    )
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