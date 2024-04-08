package me.vaimon.summarizer.util

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

object PreviewData {
    fun getLoremIpsum(lengthInWords: Int): String {
        return LoremIpsum(lengthInWords).values.joinToString()
    }
}