package com.helios.sunverta.android.features.scan.di

import com.helios.sunverta.android.features.scan.domain.AndroidImageTranslator
import com.helios.sunverta.android.features.scan.domain.MLKitTextRecognitionService
import com.helios.sunverta.features.scantranslate.domain.ImageTranslator
import com.helios.sunverta.features.scantranslate.domain.TextRecognitionService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ScanTranslateModule {

    @Binds
    abstract fun bindsTextRecognitionService(textRecognitionService: MLKitTextRecognitionService): TextRecognitionService

    @Binds
    abstract fun bindImageTranslator(imageTranslator: AndroidImageTranslator): ImageTranslator
}