package com.helios.kmptranslator.android.app.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.helios.kmptranslator.android.app.navigation.AppNavHost
import com.helios.kmptranslator.android.app.navigation.TopLevelDestination

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TranslateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier.semantics { testTagsAsResourceId = true },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        AppNavHost(
            navController = navController,
            modifier = modifier
                .padding(padding)
        )
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

private fun isTopLevelDestination(destination: NavDestination?): Boolean {
    return TopLevelDestination.entries.any { topLevelDest ->
        destination?.hierarchy?.any {
            it.route?.contains(topLevelDest.name, true) ?: false
        } ?: false
    }
}