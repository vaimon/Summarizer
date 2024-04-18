package me.vaimon.summarizer.presentation.screens.scanner.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import me.vaimon.summarizer.presentation.theme.onTranslucentBackground
import me.vaimon.summarizer.presentation.theme.translucentBackground

@Composable
fun TranslucentButton(
    iconPainter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isTranslucent: Boolean = true,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (isTranslucent)
                MaterialTheme.colorScheme.translucentBackground
            else
                MaterialTheme.colorScheme.background,
            contentColor = if (isTranslucent)
                MaterialTheme.colorScheme.onTranslucentBackground
            else
                MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
    ) {
        Icon(painter = iconPainter, contentDescription = null)
    }
}