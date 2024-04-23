package me.vaimon.summarizer.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Float.toDp(): Dp {
    return (this / Resources.getSystem().displayMetrics.density).dp
}

fun Dp.toPx(): Int = (this.value * Resources.getSystem().displayMetrics.density).toInt()