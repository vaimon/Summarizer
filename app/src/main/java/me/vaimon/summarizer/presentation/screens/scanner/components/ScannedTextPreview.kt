package me.vaimon.summarizer.presentation.screens.scanner.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.theme.bodySecondaryMedium

@Composable
fun ScannedTextPreview(
    scannedText: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        scannedText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
            )
        } ?: run {
            Text(
                text = stringResource(R.string.desc_error_scan),
                style = MaterialTheme.typography.bodySecondaryMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}