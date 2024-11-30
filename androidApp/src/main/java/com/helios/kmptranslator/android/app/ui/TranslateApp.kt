package com.helios.kmptranslator.android.app.ui

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.helios.kmptranslator.android.app.navigation.AppNavHost
import com.helios.kmptranslator.android.app.navigation.TopLevelDestination
import com.helios.kmptranslator.android.app.navigation.TranslateAppNavigationSuiteScaffold
import com.helios.kmptranslator.android.features.scantranslate.navigation.navigateToScanTranslate
import com.helios.kmptranslator.android.features.settings.navigation.navigateToSettings
import com.helios.kmptranslator.android.features.texttranslate.navigation.navigateToTextTranslate
import com.helios.kmptranslator.android.features.voicetranslate.navigation.navigateToVoiceTranslate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TranslateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val isTopLevelDestination = isTopLevelDestination(currentDestination)

    Log.d("TranslateApp", "currentDestination: $currentDestination")
    TranslateAppNavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                item(
                    selected = currentDestination.isTopLevelDestinationInHierarchy(destination),
                    onClick = {
                        val topLevelNavOptions = navOptions {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                        when (destination) {
                            TopLevelDestination.TEXT_TRANSLATE -> navController.navigateToTextTranslate(
                                topLevelNavOptions
                            )

                            TopLevelDestination.VOICE_TRANSLATE -> navController.navigateToVoiceTranslate(
                                topLevelNavOptions
                            )

                            TopLevelDestination.SCAN_TRANSLATE -> navController.navigateToScanTranslate(
                                topLevelNavOptions
                            )

                            TopLevelDestination.SETTINGS -> navController.navigateToSettings(
                                topLevelNavOptions
                            )
                        }
                    },
                    icon = { Icon(destination.unselectedIcon, contentDescription = null) },
                    selectedIcon = {
                        Icon(
                            destination.selectedIcon,
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(id = destination.titleTextId)) },
                    modifier = Modifier.testTag("NavItem")
                )
            }
        },
        windowAdaptiveInfo = windowAdaptiveInfo,
        isTopLevelDestination = isTopLevelDestination
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