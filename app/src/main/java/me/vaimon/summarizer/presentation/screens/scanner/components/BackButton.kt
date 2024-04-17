package me.vaimon.summarizer.presentation.screens.scanner.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.vaimon.summarizer.presentation.theme.onTranslucentBackground
import me.vaimon.summarizer.presentation.theme.translucentBackground

@Composable
fun BackButton(
    isTranslucent: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
    }
}