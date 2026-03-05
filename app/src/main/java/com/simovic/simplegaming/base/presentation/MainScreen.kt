package com.simovic.simplegaming.base.presentation

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
import androidx.navigation.toRoute
import com.simovic.simplegaming.BuildConfig
import com.simovic.simplegaming.base.data.FabConfig
import com.simovic.simplegaming.base.presentation.navigation.NavigationScreens
import com.simovic.simplegaming.base.presentation.util.NavigationDestinationLogger
import com.simovic.simplegaming.feature.album.presentation.screen.albumdetail.AlbumDetailScreen
import com.simovic.simplegaming.feature.album.presentation.screen.albumlist.AlbumListScreen
import com.simovic.simplegaming.feature.birthday.presentation.screen.birthdayadd.BirthDayAddScreen
import com.simovic.simplegaming.feature.birthday.presentation.screen.birthdaylist.BirthDayScreen
import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem
import com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeed.LiveFeedScreen
import com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeeddetails.LiveFeedDetailsScreen
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
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    actions: MainActions,
    scaffoldController: ScaffoldController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreens.LiveFeed,
        modifier = modifier,
    ) {
        composable<NavigationScreens.AlbumList> {
            AlbumListScreen(
                onNavigateToAlbumDetail = { artist, album, mbId ->
                    actions.toAlbumDetails(artist, album, mbId)
                },
            )
        }
        composable<NavigationScreens.AlbumDetail> { backStackEntry ->
            val detail = backStackEntry.toRoute<NavigationScreens.AlbumDetail>()
            AlbumDetailScreen(
                albumName = detail.albumName,
                artistName = detail.artistName,
                albumMbId = detail.albumMbId ?: "",
                onBackClick = { actions.navigateUp() },
            )
        }
        composable<NavigationScreens.LiveFeed> {
            LiveFeedScreen(
                onNavigateToDetails = { feedItem ->
                    actions.toLiveFeedDetails(feedItem)
                },
            )
        }
        composable<NavigationScreens.LiveFeedDetail> {
            LiveFeedDetailsScreen(
                onNavigateBack = {
                    actions.navigateUp()
                },
            )
        }
        composable<NavigationScreens.BirthDayList> {
            LaunchedEffect(Unit) {
                scaffoldController.setFabConfig(
                    config = FabConfig(onClick = { actions.toBirthDayAdd() }),
                )
            }
            DisposableEffect(Unit) {
                onDispose { scaffoldController.clearFabConfig() }
            }
            BirthDayScreen()
        }
        composable<NavigationScreens.BirthDayAdd> {
            BirthDayAddScreen(
                onFinish = { actions.navigateUp() },
            )
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
    val toAlbumDetails: (String, String, String?) -> Unit = { artist, album, mbId ->
        navController.navigate(NavigationScreens.AlbumDetail(artist, album, mbId))
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val toLiveFeedDetails: (FeedItem) -> Unit = { feedItem ->
        navController.navigate(
            NavigationScreens.LiveFeedDetail(
                guid = feedItem.guid,
                title = feedItem.title,
                link = feedItem.link,
                pubDate = feedItem.pubDate,
                description = feedItem.description,
            ),
        )
    }

    val toBirthDayAdd: () -> Unit = {
        navController.navigate(NavigationScreens.BirthDayAdd)
    }
}
