package me.vaimon.summarizer.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import me.vaimon.summarizer.R

@Composable
fun SummarizationResultViewer(
    inputText: String,
    processedText: String,
    compressionRate: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TextViewer(
            text = inputText,
            title = stringResource(R.string.title_original_text),
            modifier = Modifier.weight(2f)
        )
        TextViewer(
            text = processedText,
            title = stringResource(R.string.title_summarized_text),
            subtitle = compressionRate?.let { rate ->
                buildAnnotatedString {
                    append(stringResource(id = R.string.desc_compression_rate))
                    append(" ")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Black))
                    append(rate.toString())
                    append("%")
                    toAnnotatedString()
                }
            },
            modifier = Modifier.weight(3f)
        )
    }
}