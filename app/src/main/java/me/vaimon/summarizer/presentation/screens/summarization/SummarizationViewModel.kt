package me.vaimon.summarizer.presentation.screens.summarization

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.vaimon.summarizer.domain.usecase.ReadInputTextUseCase
import javax.inject.Inject

@HiltViewModel
class SummarizationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readInputTextUseCase: ReadInputTextUseCase
) : ViewModel() {
    private val pathToInputText =
        checkNotNull(savedStateHandle.get<String>(SummarizationDestination.argName))

    private val _inputText: MutableStateFlow<String> = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val _processedText: MutableStateFlow<String?> = MutableStateFlow(null)
    val processingState = _processedText.map { it == null }
        .stateIn(viewModelScope, SharingStarted.Lazily, true)
    val processedText = _processedText.asStateFlow()

    init {
        viewModelScope.launch {
            delay(5000)
            _inputText.value = readInputTextUseCase(pathToInputText)
            _processedText.value = inputText.value.filterIndexed { i, _ -> i % 2 == 0 }
        }
    }
}