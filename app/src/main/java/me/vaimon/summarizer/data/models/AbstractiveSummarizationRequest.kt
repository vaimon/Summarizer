package me.vaimon.summarizer.data.models

import com.google.gson.annotations.SerializedName

data class AbstractiveSummarizationRequest(
    @SerializedName("text") val text: String,
)
