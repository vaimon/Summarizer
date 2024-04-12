package me.vaimon.summarizer.manager

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
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
    suspend fun scanImageForText(imageProxy: ImageProxy): String {
        val mediaImage = imageProxy.image ?: throw IllegalStateException()
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        return suspendCoroutine { cont ->
            textRecognizer.process(image).addOnSuccessListener {
                cont.resume(it.text)
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
        }
    }
}