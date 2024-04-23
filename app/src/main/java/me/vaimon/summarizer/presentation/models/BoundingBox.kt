package me.vaimon.summarizer.presentation.models

import android.graphics.Point
import androidx.compose.ui.unit.Dp
import me.vaimon.summarizer.util.models.PointDp
import me.vaimon.summarizer.util.toPx

data class BoundingBox(
    val topLeftCorner: PointDp,
    val width: Dp,
    val height: Dp,
    val widthRatio: Float,
    val heightRatio: Float
) {
    val scaledValues: Triple<Point, Int, Int>
        get() = Triple(
            Point(
                (topLeftCorner.x.toPx() * widthRatio).toInt(),
                (topLeftCorner.y.toPx() * heightRatio).toInt()
            ),
            (width.toPx() * widthRatio).toInt(),
            (height.toPx() * heightRatio).toInt(),
        )

}
