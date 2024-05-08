package me.vaimon.summarizer.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.models.SummarizedText

@Composable
fun SummarizationHistoryGrid(
    summarizationHistory: List<SummarizedText>,
    onHistoryEntryClick: (SummarizedText) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.title_summarization_history),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            modifier = Modifier
        ) {
            items(summarizationHistory.reversed()) {
                SummarizedItem(it, onClick = onHistoryEntryClick)
            }
        }
    }
}