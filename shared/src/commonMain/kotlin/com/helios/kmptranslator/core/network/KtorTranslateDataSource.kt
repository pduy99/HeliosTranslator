package com.helios.kmptranslator.core.network

import com.helios.kmptranslator.BuildKonfig
import com.helios.kmptranslator.core.data.datasource.TranslateDataSource
import com.helios.kmptranslator.core.network.dto.TranslateDto
import com.helios.kmptranslator.core.network.dto.TranslatedDto
import com.helios.kmptranslator.core.data.error.TranslateError
import com.helios.kmptranslator.core.data.error.TranslateException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
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
    ): String {
        val result: HttpResponse = try {
            httpClient.post {
                url(BuildKonfig.BASE_URL + "/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateDto(
                        textToTranslate = fromText,
                        sourceLanguageCode = fromLanguageCode,
                        targetLanguageCode = toLanguageCode
                    )
                )
            }
        } catch (ex: IOException) {
            throw TranslateException(TranslateError.SERVICE_UNAVAILABLE)
        }

        when (result.status.value) {
            in 200..299 -> Unit
            500 -> throw TranslateException(TranslateError.SERVER_ERROR)
            in 400..499 -> throw TranslateException(TranslateError.CLIENT_ERROR)
            else -> throw TranslateException(TranslateError.UNKNOWN_ERROR)
        }

        return try {
            result.body<TranslatedDto>().translatedText
        } catch (e: Exception) {
            throw TranslateException(TranslateError.SERVER_ERROR)
        }
    }
}