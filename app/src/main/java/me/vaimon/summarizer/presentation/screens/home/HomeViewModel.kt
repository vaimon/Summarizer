package me.vaimon.summarizer.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.vaimon.summarizer.domain.usecase.SaveInputTextUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveInputTextUseCase: SaveInputTextUseCase
) : ViewModel() {
    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onInputTextChanged(newText: String){
        _inputText.value = newText
    }

    fun onBtnSummarizeClick() {
        viewModelScope.launch {
            val outputFileName = saveInputTextUseCase(inputText.value)
            _uiState.value = _uiState.value.copy(summarizationNavigationArg = outputFileName)
        }
    }

    fun onNavigateToSummarizationHandled() {
        _uiState.value = _uiState.value.copy(summarizationNavigationArg = null)
    }

    data class UiState(
        val summarizationNavigationArg: String? = null
    )
}