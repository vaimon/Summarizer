package me.vaimon.summarizer.util

import java.time.format.DateTimeFormatter

object Formatter {
    val labelDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM HH:mm")
}