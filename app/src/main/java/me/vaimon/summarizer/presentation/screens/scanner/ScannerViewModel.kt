package me.vaimon.summarizer.presentation.screens.scanner

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onCapture(image: ImageProxy) {
        _uiState.value = _uiState.value.copy(
            isCaptureRequired = false,
            isCameraMode = false,
            isProcessing = true
        )
    }

    fun requireCapture() {
        _uiState.value = _uiState.value.copy(isCaptureRequired = true)
    }

    data class UiState(
        val isCaptureRequired: Boolean = false,
        val isCameraMode: Boolean = true,
        val isProcessing: Boolean = false
    )
}