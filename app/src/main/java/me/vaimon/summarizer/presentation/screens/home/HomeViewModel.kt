package me.vaimon.summarizer.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.vaimon.summarizer.domain.entity.SummarizedTextEntity
import me.vaimon.summarizer.domain.usecase.GetSummarizationHistoryUseCase
import me.vaimon.summarizer.domain.usecase.ReadInputTextUseCase
import me.vaimon.summarizer.domain.usecase.SaveTextToCacheUseCase
import me.vaimon.summarizer.presentation.models.SummarizedText
import me.vaimon.summarizer.util.Mapper
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveTextToCacheUseCase: SaveTextToCacheUseCase,
    private val readInputTextUseCase: ReadInputTextUseCase,
    private val getSummarizationHistoryUseCase: GetSummarizationHistoryUseCase,
    private val summarizedTextMapper: Mapper<SummarizedText, SummarizedTextEntity>
) : ViewModel() {
    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    val summarizationHistory = getSummarizationHistoryUseCase().map { list ->
        list.map { summarizedTextMapper.from(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onInputTextChanged(newText: String) {
        _inputText.value = newText
    }

    fun onBtnSummarizeClick() {
        viewModelScope.launch {
            val outputFileName = saveTextToCacheUseCase(inputText.value)
            _uiState.value = _uiState.value.copy(summarizationNavigationArg = outputFileName)
        }
    }

    fun onNavigateToSummarizationHandled() {
        _uiState.value = _uiState.value.copy(summarizationNavigationArg = null)
    }

    fun onScanningResultReceived(resultFileName: String) {
        viewModelScope.launch {
            _inputText.value = readInputTextUseCase(resultFileName)
        }
    }

    fun onSummarizationHistoryEntryClick(summarizedText: SummarizedText) {
        _uiState.update { it.copy(priorSummarizationDetails = summarizedText) }
    }

    fun onHistoryEntryDetailsShown() {
        _uiState.update { it.copy(priorSummarizationDetails = null) }
    }

    data class UiState(
        val summarizationNavigationArg: String? = null,
        val priorSummarizationDetails: SummarizedText? = null
    )
}