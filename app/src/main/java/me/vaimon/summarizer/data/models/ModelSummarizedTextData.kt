package me.vaimon.summarizer.data.models

import com.google.gson.annotations.SerializedName

data class ModelSummarizedTextData(
    @SerializedName("summary") val summarizedText: String
)