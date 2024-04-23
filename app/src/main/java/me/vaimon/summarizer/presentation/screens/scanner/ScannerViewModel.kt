package me.vaimon.summarizer.presentation.screens.scanner

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.vaimon.summarizer.domain.usecase.SaveTextToCacheUseCase
import me.vaimon.summarizer.manager.MLManager
import me.vaimon.summarizer.presentation.models.BoundingBox
import me.vaimon.summarizer.util.rotate
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val saveTextToCacheUseCase: SaveTextToCacheUseCase,
    private val mlManager: MLManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _scannedText = MutableStateFlow<String?>(null)
    val scannedText = _scannedText.asStateFlow()

    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage = _capturedImage.asStateFlow()

    private val _boundingBox = MutableStateFlow<BoundingBox?>(null)
    val boundingBox = _boundingBox.asStateFlow()

    fun onCapture(image: ImageProxy) {
        viewModelScope.launch {
            _capturedImage.value =
                image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())
            _uiState.value = _uiState.value.copy(
                isCaptureRequired = false,
                scannerMode = ScannerMode.Crop,
            )
        }
    }

    fun onBoundingBoxChanged(newBoundingBox: BoundingBox) {
        _boundingBox.value = newBoundingBox
    }

    fun requireCapture() {
        _uiState.value = _uiState.value.copy(isCaptureRequired = true)
    }

    fun onBtnCopyClick() {
        viewModelScope.launch {
            val outputFileName =
                saveTextToCacheUseCase(_scannedText.value ?: throw IllegalStateException())
            _uiState.value = _uiState.value.copy(backNavigationArg = outputFileName)
        }
    }

    fun onBtnScanClick() {
        _uiState.value = _uiState.value.copy(
            scannerMode = ScannerMode.Preview,
        )
        viewModelScope.launch {
            try {
                _capturedImage.value?.let {
                    _scannedText.value = mlManager.scanImageForText(it)
                }
            } catch (e: Exception) {
                Log.e("ScannerML", "Error processing image", e)
            }

            _uiState.value = _uiState.value.copy(
                isProcessing = false
            )
        }
    }

    fun onNavigateBackHandled() {
        _uiState.value = _uiState.value.copy(backNavigationArg = null)
    }

    data class UiState(
        val isCaptureRequired: Boolean = false,
        val scannerMode: ScannerMode = ScannerMode.Capture,
        val isProcessing: Boolean = false,
        val backNavigationArg: String? = null
    )

    enum class ScannerMode { Capture, Crop, Preview }
}