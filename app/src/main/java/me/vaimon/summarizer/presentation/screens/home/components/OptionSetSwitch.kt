package me.vaimon.summarizer.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.vaimon.summarizer.util.conditional

@Composable
fun OptionSetSwitch(
    options: List<String>,
    onOptionSelected: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        itemsIndexed(options) { index, item ->
            Text(
                text = item,
                style = MaterialTheme.typography.labelMedium,
                color = if (selectedItemIndex == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .conditional(selectedItemIndex == index) { background(MaterialTheme.colorScheme.primary) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        selectedItemIndex = index
                        onOptionSelected(index, item)
                    }
            )
        }
    }
}