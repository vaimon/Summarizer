package me.vaimon.summarizer.presentation.screens.scanner.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import me.vaimon.summarizer.presentation.models.BoundingBox
import me.vaimon.summarizer.util.models.PointDp
import me.vaimon.summarizer.util.toDp
import me.vaimon.summarizer.util.toPx

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
                        ),
                        widthRatio = oldBoundingBox.widthRatio,
                        heightRatio = oldBoundingBox.heightRatio
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
                    maxHeight * 0.9f,
                    bitmap.width / maxWidth.toPx().toFloat(),
                    bitmap.height / maxHeight.toPx().toFloat(),
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