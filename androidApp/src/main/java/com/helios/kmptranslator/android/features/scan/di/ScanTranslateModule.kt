package com.helios.kmptranslator.android.features.scan.di

import com.helios.kmptranslator.android.features.scan.domain.AndroidImageTranslator
import com.helios.kmptranslator.core.domain.usecase.TranslateUseCase
import com.helios.kmptranslator.features.scantranslate.domain.ImageTranslator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ScanTranslateModule {

    @Provides
    @ViewModelScoped
    fun provideImageTranslator(translateUseCase: TranslateUseCase): ImageTranslator = AndroidImageTranslator(
        translateUseCase = translateUseCase
    )
}