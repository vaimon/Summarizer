package me.vaimon.summarizer.presentation.screens.summarization

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.vaimon.summarizer.domain.repository.SummarizationRepository
import me.vaimon.summarizer.domain.usecase.ReadInputTextUseCase
import me.vaimon.summarizer.domain.usecase.SaveSummarizationUseCase
import me.vaimon.summarizer.presentation.models.SummarizationType
import javax.inject.Inject

@HiltViewModel
class SummarizationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readInputTextUseCase: ReadInputTextUseCase,
    private val saveSummarizationUseCase: SaveSummarizationUseCase,
    private val summarizationRepository: SummarizationRepository
) : ViewModel() {
    private val pathToInputText =
        checkNotNull(savedStateHandle.get<String>(SummarizationDestination.arg1Name))

    private val summarizationType =
        checkNotNull(savedStateHandle.get<SummarizationType>(SummarizationDestination.arg2Name))

    private val summarizationCompressionRate =
        checkNotNull(savedStateHandle.get<Float>(SummarizationDestination.arg3Name))

    private val _inputText: MutableStateFlow<String> = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val _processedText: MutableStateFlow<String?> = MutableStateFlow(null)
    val processingState = _processedText.map { it == null }
        .stateIn(viewModelScope, SharingStarted.Lazily, true)
    val processedText = _processedText.asStateFlow()

    val compressionRate = _processedText.map {
        if (inputText.value.isEmpty())
            return@map 0
        it?.length?.let { compressedLength -> 100 - (100 * compressedLength.toDouble() / inputText.value.length).toInt() }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        viewModelScope.launch {
            _inputText.value = readInputTextUseCase(pathToInputText)

            try{
                when(summarizationType){
                    SummarizationType.Extractive -> {
                        summarizationRepository.summarizeExtractive(inputText.value, summarizationCompressionRate)
                    }
                    SummarizationType.Abstractive -> {
                        summarizationRepository.summarizeAbstractive(inputText.value)
                    }
                }
            } catch(e: Exception){
                inputText.value
            }.let{
                _processedText.value = it
                saveSummarizationUseCase(inputText.value, it, compressionRate.value ?: 0)
            }
        }
    }
}