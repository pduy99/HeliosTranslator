package com.helios.kmptranslator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.helios.kmptranslator.android.app.ui.TranslateApp
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeliosTranslatorTheme(darkTheme = true) {
                TranslateApp(navController = rememberNavController())
            }
        }
    }
}