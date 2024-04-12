package me.vaimon.summarizer.presentation.screens.scanner

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

    fun onCapture(image: ImageProxy) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCaptureRequired = false,
                isCameraMode = false,
                isProcessing = true
            )

            try {
                _scannedText.value = mlManager.scanImageForText(image)
            } catch (e: Exception) {
                Log.e("ScannerML", "Error processing image", e)
            }

            _uiState.value = _uiState.value.copy(
                isProcessing = false
            )
        }
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

    fun onNavigateBackHandled() {
        _uiState.value = _uiState.value.copy(backNavigationArg = null)
    }

    data class UiState(
        val isCaptureRequired: Boolean = false,
        val isCameraMode: Boolean = true,
        val isProcessing: Boolean = false,
        val backNavigationArg: String? = null
    )
}