package com.helios.sunverta.core.network.deepl

import com.helios.sunverta.BuildKonfig
import com.helios.sunverta.core.data.datasource.RemoteLanguageDataSource
import com.helios.sunverta.core.network.deepl.dto.LanguageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DeepLLanguageDataSourceImpl(
    private val httpClient: HttpClient,
) : RemoteLanguageDataSource {
    override suspend fun getAvailableLanguages(): List<LanguageDto> {
        val response = httpClient.get {
            url(BuildKonfig.BASE_URL + "/languages")
            contentType(ContentType.Application.Json)
        }

        return when (response.status.value) {
            in 200..299 -> {
                response.body<List<LanguageDto>>()
            }

            else -> {
                emptyList()
            }
        }
    }

}