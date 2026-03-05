package com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeeddetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.LoadingIndicator
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveFeedDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: LiveFeedDetailsViewModel = koinViewModel()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.news_feed)) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (val currentUiState = uiState) {
                LiveFeedDetailsUiState.Loading -> LoadingIndicator()
                is LiveFeedDetailsUiState.Content -> LiveFeedDetailsContent(currentUiState)
            }
        }
    }
}

@Composable
private fun LiveFeedDetailsContent(content: LiveFeedDetailsUiState.Content) {
    Column {
        Text(content.feed.pubDate, style = AppTheme.typo.body3, color = AppTheme.color.textMuted)
        Text(content.feed.title, style = AppTheme.typo.head2, color = AppTheme.color.textMain)
        Text(
            content.feed.description,
            style = AppTheme.typo.body2,
            color = AppTheme.color.textMain,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    AppPreview {
        LiveFeedDetailsContent(
            LiveFeedDetailsUiState.Content(
                feed =
                    FeedItem(
                        guid = "guid id is this",
                        title = "Test",
                        link = "www.google.com",
                        pubDate = "05.05.1995",
                        description = "Hello this is a long description",
                    ),
            ),
        )
    }
}
