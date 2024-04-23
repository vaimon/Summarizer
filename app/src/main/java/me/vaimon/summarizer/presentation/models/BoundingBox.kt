package me.vaimon.summarizer.presentation.models

import androidx.compose.ui.unit.Dp
import me.vaimon.summarizer.util.PointDp

data class BoundingBox(
    val topLeftCorner: PointDp,
    val width: Dp,
    val height: Dp
)
