package me.vaimon.summarizer.presentation.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.vaimon.summarizer.presentation.models.SummarizedText
import me.vaimon.summarizer.presentation.theme.OnSurfaceSecondary
import me.vaimon.summarizer.util.Formatter
import me.vaimon.summarizer.util.conditional

@Composable
fun SummarizedItem(
    text: SummarizedText,
    modifier: Modifier = Modifier,
    onClick: ((SummarizedText) -> Unit)? = null,
) {
    Card(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(0.25.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
            .width(256.dp)
            .conditional(onClick != null) {
                clickable { onClick?.invoke(text) }
            }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Text(
                text = text.timestamp.format(Formatter.labelDateTimeFormatter),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = text.summarizedText,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceSecondary,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}