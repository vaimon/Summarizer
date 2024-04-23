package me.vaimon.summarizer.manager

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MLManager @Inject constructor(
    private val textRecognizer: TextRecognizer
) {

    @OptIn(ExperimentalGetImage::class)
    suspend fun scanImageForText(image: Bitmap): String {
        return suspendCoroutine { cont ->
            textRecognizer.process(InputImage.fromBitmap(image, 0)).addOnSuccessListener {
                cont.resume(it.text)
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
        }
    }
}