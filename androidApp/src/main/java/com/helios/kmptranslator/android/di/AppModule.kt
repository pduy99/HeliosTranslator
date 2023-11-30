package com.helios.kmptranslator.android.di

import android.app.Application
import com.helios.kmptranslator.database.TranslateDatabase
import com.helios.kmptranslator.translate.data.history.SqlDelightHistoryDataSource
import com.helios.kmptranslator.translate.data.local.DatabaseDriverFactory
import com.helios.kmptranslator.translate.data.remote.HttpClientFactory
import com.helios.kmptranslator.translate.data.translate.KtorTranslateClient
import com.helios.kmptranslator.translate.domain.history.HistoryDataSource
import com.helios.kmptranslator.translate.domain.translate.TranslateClient
import com.helios.kmptranslator.translate.domain.translate.TranslateUseCase
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient {
        return HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun providesTranslateClient(httpClient: HttpClient): TranslateClient {
        return KtorTranslateClient(httpClient)
    }

    @Provides
    @Singleton
    fun providesDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun providesHistoryDataSource(sqlDriver: SqlDriver): HistoryDataSource {
        return SqlDelightHistoryDataSource(TranslateDatabase(driver = sqlDriver))
    }

    @Provides
    @Singleton
    fun providesTranslateUseCase(
        translateClient: TranslateClient,
        dataSource: HistoryDataSource
    ): TranslateUseCase {
        return TranslateUseCase(translateClient, dataSource)
    }
}