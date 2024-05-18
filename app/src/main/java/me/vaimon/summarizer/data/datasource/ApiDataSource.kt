package me.vaimon.summarizer.data.datasource

import me.vaimon.summarizer.data.models.AbstractiveSummarizationRequest
import me.vaimon.summarizer.data.models.ExtractiveSummarizationRequest
import me.vaimon.summarizer.data.models.ModelSummarizedTextData
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiDataSource {
    companion object {
        const val BASE_URL = "https://95.25.50.113:10000/"
    }

    @POST("/summarize/abstractive")
    suspend fun summarizeAbstractive(@Body request: AbstractiveSummarizationRequest): ModelSummarizedTextData

    @POST("/summarize/extractive")
    suspend fun summarizeExtractive(@Body request: ExtractiveSummarizationRequest): ModelSummarizedTextData
}