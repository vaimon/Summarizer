package me.vaimon.summarizer.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.vaimon.summarizer.R
import me.vaimon.summarizer.presentation.screens.components.PrimaryActionButton
import me.vaimon.summarizer.presentation.screens.components.SecondaryActionButton
import me.vaimon.summarizer.presentation.theme.secondaryBackground

@Composable
fun InputTextField(
    inputText: String,
    onInputTextChanged: (String) -> Unit,
    onBtnSummarizeClick: () -> Unit,
    onBtnCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val clipboardManager: ClipboardManager = LocalClipboardManager.current

        TextField(
            value = inputText,
            onValueChange = onInputTextChanged,
            placeholder = { Text(stringResource(R.string.placeholder_input_text)) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryBackground
            ),
            minLines = 4,
            maxLines = 8,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            SecondaryActionButton(
                icon = R.drawable.ic_paste,
                enabled = clipboardManager.hasText(),
                onClick = {
                    clipboardManager.getText()?.text?.let(onInputTextChanged)
                })
            PrimaryActionButton(
                icon = R.drawable.ic_compress,
                onClick = onBtnSummarizeClick,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            SecondaryActionButton(icon = R.drawable.ic_camera, onClick = onBtnCameraClick)
        }
    }
}