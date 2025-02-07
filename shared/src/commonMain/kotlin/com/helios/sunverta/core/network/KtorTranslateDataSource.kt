package com.helios.sunverta.core.network

import com.helios.sunverta.BuildKonfig
import com.helios.sunverta.core.data.datasource.TranslateDataSource
import com.helios.sunverta.core.domain.util.Result
import com.helios.sunverta.core.network.dto.TranslateDto
import com.helios.sunverta.core.network.dto.TranslatedDto
import com.helios.sunverta.features.translate.TranslateError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException

class KtorTranslateDataSource(
    private val httpClient: HttpClient,
) : TranslateDataSource {
    override suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError.Network> {
        try {
            val isAllUppercase = fromText == fromText.uppercase()
            val isFirstLetterUppercase =
                fromText.first().isUpperCase() && fromText.drop(1) == fromText.drop(1).lowercase()
            val lowercaseText = fromText.lowercase()

            val response = httpClient.post {
                url(BuildKonfig.BASE_URL + "/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateDto(
                        textToTranslate = lowercaseText,
                        sourceLanguageCode = fromLanguageCode,
                        targetLanguageCode = toLanguageCode
                    )
                )
            }

            return when (response.status.value) {
                in 200..299 -> {
                    val result = response.body<TranslatedDto>()
                    val finalText = when {
                        isAllUppercase -> result.translatedText.uppercase()
                        isFirstLetterUppercase -> result.translatedText.replaceFirstChar {
                            if (it.isLowerCase()) it.uppercase() else it.toString()
                        }

                        else -> result.translatedText
                    }
                    Result.Success(finalText)
                }

                500 -> Result.Failure(TranslateError.Network.SERVER_ERROR)
                in 400..499 -> Result.Failure(TranslateError.Network.CLIENT_ERROR)
                else -> Result.Failure(TranslateError.Network.UNKNOWN_ERROR)
            }

        } catch (ex: IOException) {
            return Result.Failure(TranslateError.Network.SERVER_ERROR)
        }
    }
}