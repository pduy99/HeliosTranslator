package com.helios.sunverta.core.network.deepl

import com.helios.sunverta.BuildKonfig
import com.helios.sunverta.core.data.datasource.TranslateDataSource
import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.network.deepl.dto.TranslatedDto
import com.helios.sunverta.core.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlinx.io.IOException

class DeepLTranslateDataSource(
    private val httpClient: HttpClient,
) : TranslateDataSource {

    private companion object {
        const val MAX_RETRIES = 3
        const val INITIAL_DELAY_MS = 1000L
        const val MAX_DELAY_MS = 15000L
        const val BACKOFF_FACTOR = 1.5
    }

    override suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError> {
        var currentDelay = INITIAL_DELAY_MS
        var attemptsRemaining = MAX_RETRIES
        while (attemptsRemaining > 0) {
            try {
                val response = httpClient.post {
                    url(BuildKonfig.BASE_URL + "/translate")
                    contentType(ContentType.Application.FormUrlEncoded)
                    setBody(
                        FormDataContent(
                            Parameters.build {
                                append("text", fromText)
                                append("source_lang", fromLanguageCode)
                                append("target_lang", toLanguageCode)
                            }
                        )
                    )
                }

                return when (response.status.value) {
                    in 200..299 -> {
                        val result = response.body<TranslatedDto>()
                        Result.Success(result.translations.first().text)
                    }

                    429 -> {
                        // Handle rate limiting specifically
                        attemptsRemaining--
                        if (attemptsRemaining <= 0) {
                            return Result.Failure(TranslateError.Network.CLIENT_ERROR)
                        }

                        // Extract Retry-After header if available
                        val retryAfterSeconds = response.headers["Retry-After"]?.toLongOrNull()
                        val delayTime = retryAfterSeconds?.times(1000) ?: currentDelay

                        delay(delayTime)
                        currentDelay =
                            (currentDelay * BACKOFF_FACTOR).toLong().coerceAtMost(MAX_DELAY_MS)
                        continue
                    }

                    500 -> Result.Failure(TranslateError.Network.SERVER_ERROR)
                    in 400..499 -> Result.Failure(TranslateError.Network.CLIENT_ERROR)
                    else -> Result.Failure(TranslateError.Network.UNKNOWN_ERROR)
                }

            } catch (ex: IOException) {
                attemptsRemaining--
                if (attemptsRemaining <= 0) {
                    return Result.Failure(TranslateError.Network.SERVER_ERROR)
                }

                delay(currentDelay)
                currentDelay = (currentDelay * BACKOFF_FACTOR).toLong().coerceAtMost(MAX_DELAY_MS)
            } catch (ex: Exception) {
                return Result.Failure(TranslateError.Network.UNKNOWN_ERROR)
            }
        }
        return Result.Failure(TranslateError.Network.SERVER_ERROR)
    }

}