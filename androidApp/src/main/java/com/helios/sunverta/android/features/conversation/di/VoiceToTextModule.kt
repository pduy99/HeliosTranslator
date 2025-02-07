package com.helios.sunverta.android.features.conversation.di

import android.app.Application
import com.helios.sunverta.android.features.conversation.domain.AndroidVoiceToTextParser
import com.helios.sunverta.features.voicetotext.domain.VoiceToTextParser
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