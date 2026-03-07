package com.simovic.simplegaming.base.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simovic.simplegaming.BuildConfig
import com.simovic.simplegaming.base.data.FabConfig
import com.simovic.simplegaming.base.presentation.navigation.NavigationScreens
import com.simovic.simplegaming.base.presentation.util.NavigationDestinationLogger
import com.simovic.simplegaming.feature.games.presentation.screen.addgame.AddGameScreen
import com.simovic.simplegaming.feature.games.presentation.screen.favouritegames.FavouriteGamesScreen
import com.simovic.simplegaming.feature.reels.presentation.screen.reels.ReelsScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainShowcaseScreen(
    modifier: Modifier = Modifier,
    scaffoldController: ScaffoldController = koinViewModel(),
) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    if (BuildConfig.DEBUG) {
        addOnDestinationChangedListener(navController)
    }

    val fabConfig by scaffoldController.fabConfig.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            if (fabConfig.isVisible) {
                FloatingActionButton(onClick = fabConfig.onClick) {
                    Icon(fabConfig.icon, contentDescription = null)
                }
            }
        },
    ) { innerPadding ->
        MainNavHost(
            navController = navController,
            actions = actions,
            scaffoldController = scaffoldController,
            innerPadding = innerPadding,
        )
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    actions: MainActions,
    scaffoldController: ScaffoldController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreens.Reels,
        modifier = modifier.fillMaxSize(),
    ) {
        composable<NavigationScreens.FavouriteGames> {
            LaunchedEffect(Unit) {
                scaffoldController.setFabConfig(
                    config = FabConfig(onClick = { actions.toAddGame() }),
                )
            }
            DisposableEffect(Unit) {
                onDispose { scaffoldController.clearFabConfig() }
            }
            FavouriteGamesScreen(modifier = Modifier.padding(innerPadding))
        }
        composable<NavigationScreens.AddGame> {
            AddGameScreen(
                onNavigateBack = { actions.navigateUp() },
                modifier = Modifier.padding(innerPadding),
            )
        }
        composable<NavigationScreens.Reels> {
            ReelsScreen(contentBottomPadding = innerPadding.calculateBottomPadding())
        }
    }
}

private fun addOnDestinationChangedListener(navController: NavController) {
    navController.addOnDestinationChangedListener { _, destination, arguments ->
        NavigationDestinationLogger.logDestinationChange(destination, arguments)
    }
}

data class MainActions(
    private val navController: NavHostController,
) {
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val toAddGame: () -> Unit = {
        navController.navigate(NavigationScreens.AddGame)
    }
}
