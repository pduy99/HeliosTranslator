package com.helios.kmptranslator.android.voicetotext.di

import android.app.Application
import com.helios.kmptranslator.android.voicetotext.domain.AndroidVoiceToTextParser
import com.helios.kmptranslator.voicetotext.domain.VoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class VoiceToTextModule {

    @Provides
    @ViewModelScoped
    fun provideVoiceToTextParser(app: Application): VoiceToTextParser {
        return AndroidVoiceToTextParser(app)
    }

}