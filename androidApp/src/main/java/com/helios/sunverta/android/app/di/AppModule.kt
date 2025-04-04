package com.helios.sunverta.android.app.di

import android.app.Application
import com.helios.sunverta.core.data.datasource.LocalLanguageDataSource
import com.helios.sunverta.core.data.datasource.RemoteLanguageDataSource
import com.helios.sunverta.core.data.datasource.TranslateDataSource
import com.helios.sunverta.core.data.datasource.TranslateHistoryDataSource
import com.helios.sunverta.core.data.repository.LanguageRepository
import com.helios.sunverta.core.data.repository.LanguageRepositoryImpl
import com.helios.sunverta.core.data.repository.OfflineTranslateHistoryRepository
import com.helios.sunverta.core.data.repository.RemoteTranslateRepository
import com.helios.sunverta.core.data.repository.TranslateHistoryRepository
import com.helios.sunverta.core.data.repository.TranslateRepository
import com.helios.sunverta.core.database.DatabaseDriverFactory
import com.helios.sunverta.core.database.LocalLanguageDataSourceImpl
import com.helios.sunverta.core.database.SqlDelightTranslateHistoryDataSource
import com.helios.sunverta.core.domain.usecase.GetAndCleanTranslateHistoryUseCase
import com.helios.sunverta.core.domain.usecase.GetTranslateHistoryUseCase
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.core.network.HttpClientFactory
import com.helios.sunverta.core.network.deepl.DeepLLanguageDataSourceImpl
import com.helios.sunverta.core.network.deepl.DeepLTranslateDataSource
import com.helios.sunverta.database.TranslateDatabase
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
        return DeepLTranslateDataSource(httpClient)
    }

    @Provides
    @Singleton
    fun providesDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun providesTranslateDatabase(sqlDriver: SqlDriver): TranslateDatabase {
        return TranslateDatabase(driver = sqlDriver)
    }

    @Provides
    @Singleton
    fun providesHistoryDataSource(database: TranslateDatabase): TranslateHistoryDataSource {
        return SqlDelightTranslateHistoryDataSource(database)
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
    fun providesGetAndCleanTranslateHistoryUseCase(
        translateHistoryRepository: TranslateHistoryRepository
    ): GetAndCleanTranslateHistoryUseCase {
        return GetAndCleanTranslateHistoryUseCase(translateHistoryRepository)
    }

    @Provides
    @Singleton
    fun providesTranslateRepository(translateDataSource: TranslateDataSource): TranslateRepository {
        return RemoteTranslateRepository(translateDataSource)
    }

    @Provides
    @Singleton
    fun providesRemoteLanguageDataSource(
        httpClient: HttpClient,
    ): RemoteLanguageDataSource {
        return DeepLLanguageDataSourceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun providesLocalLanguageDataSource(
        database: TranslateDatabase
    ): LocalLanguageDataSource {
        return LocalLanguageDataSourceImpl(database)
    }

    @Provides
    @Singleton
    fun providesLanguageRepository(
        remoteLanguageDataSource: RemoteLanguageDataSource,
        localLanguageDataSource: LocalLanguageDataSource
    ): LanguageRepository {
        return LanguageRepositoryImpl(
            remoteLanguageDataSource = remoteLanguageDataSource,
            localLanguageDataSource = localLanguageDataSource
        )
    }
}