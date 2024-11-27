package com.helios.kmptranslator.android.app.di

import android.app.Application
import com.helios.kmptranslator.core.data.datasource.TranslateDataSource
import com.helios.kmptranslator.core.data.datasource.TranslateHistoryDataSource
import com.helios.kmptranslator.core.data.repository.OfflineTranslateHistoryRepository
import com.helios.kmptranslator.core.data.repository.RemoteTranslateRepository
import com.helios.kmptranslator.core.data.repository.TranslateHistoryRepository
import com.helios.kmptranslator.core.data.repository.TranslateRepository
import com.helios.kmptranslator.core.database.DatabaseDriverFactory
import com.helios.kmptranslator.core.database.SqlDelightTranslateHistoryDataSource
import com.helios.kmptranslator.core.domain.usecase.GetTranslateHistoryUseCase
import com.helios.kmptranslator.core.domain.usecase.TranslateUseCase
import com.helios.kmptranslator.core.network.HttpClientFactory
import com.helios.kmptranslator.core.network.KtorTranslateDataSource
import com.helios.kmptranslator.database.TranslateDatabase
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
    fun providesTranslateDataSource(httpClient: HttpClient): TranslateDataSource {
        return KtorTranslateDataSource(httpClient)
    }

    @Provides
    @Singleton
    fun providesDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun providesHistoryDataSource(sqlDriver: SqlDriver): TranslateHistoryDataSource {
        return SqlDelightTranslateHistoryDataSource(TranslateDatabase(driver = sqlDriver))
    }

    @Provides
    @Singleton
    fun providesTranslateUseCase(
        translateRepository: TranslateRepository,
        translateHistoryRepository: TranslateHistoryRepository
    ): TranslateUseCase {
        return TranslateUseCase(translateRepository, translateHistoryRepository)
    }


    @Provides
    @Singleton
    fun providesTranslateHistoryRepository(
        translateHistoryDataSource: TranslateHistoryDataSource
    ): TranslateHistoryRepository {
        return OfflineTranslateHistoryRepository(translateHistoryDataSource)
    }

    @Provides
    @Singleton
    fun providesGetTranslateHistoryUseCase(translateHistoryRepository: TranslateHistoryRepository): GetTranslateHistoryUseCase {
        return GetTranslateHistoryUseCase(translateHistoryRepository)
    }

    @Provides
    @Singleton
    fun providesTranslateRepository(translateDataSource: TranslateDataSource): TranslateRepository {
        return RemoteTranslateRepository(translateDataSource)
    }
}