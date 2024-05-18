package me.vaimon.summarizer.data.models

import com.google.gson.annotations.SerializedName

data class ExtractiveSummarizationRequest(
    @SerializedName("text") val text: String,
    @SerializedName("compression_rate") val compressionRate: Float
)
