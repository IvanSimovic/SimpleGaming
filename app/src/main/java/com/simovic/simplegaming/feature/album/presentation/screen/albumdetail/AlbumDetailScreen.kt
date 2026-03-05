package com.simovic.simplegaming.feature.album.presentation.screen.albumdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.ErrorAnim
import com.simovic.simplegaming.base.presentation.compose.composable.LoadingIndicator
import com.simovic.simplegaming.base.presentation.compose.composable.PlaceholderImage
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.feature.album.domain.model.Tag
import com.simovic.simplegaming.feature.album.domain.model.Track
import com.simovic.simplegaming.feature.album.presentation.util.TimeUtil
import org.koin.androidx.compose.koinViewModel

@Composable
fun ToolBarFavorite(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (icon, descriptionRes) =
        when (isFavorite) {
            true -> Icons.Filled.Close to R.string.remove_from_favorite
            false -> Icons.Filled.Star to R.string.add_to_favorite
        }

    IconButton(modifier = modifier, onClick = onFavoriteClick) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(descriptionRes),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumName: String,
    artistName: String,
    albumMbId: String?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    val viewModel: AlbumDetailViewModel = koinViewModel()
    LaunchedEffect(Unit) {
        viewModel.onInit(albumName, artistName, albumMbId)
    }

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = albumName) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.album_detail_back),
                    )
                }
            },
            actions = {
                (uiState as? AlbumDetailUiState.Content)?.let { content ->
                    ToolBarFavorite(
                        isFavorite = content.isFavorite,
                        onFavoriteClick = viewModel::addToFavorites,
                    )
                }
            },
        )
        when (val currentUiState = uiState) {
            AlbumDetailUiState.Error -> {
                ErrorAnim()
            }

            AlbumDetailUiState.Loading -> {
                LoadingIndicator()
            }

            is AlbumDetailUiState.Content -> {
                AlbumDetailContent(
                    content = currentUiState,
                )
            }
        }
    }
}

@Composable
private fun AlbumDetailContent(
    content: AlbumDetailUiState.Content,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(horizontal = Dimen.screenContentPadding)
                .verticalScroll(rememberScrollState()),
    ) {
        ElevatedCard(
            modifier =
                Modifier
                    .padding(Dimen.spaceM)
                    .wrapContentSize()
                    .size(320.dp)
                    .align(CenterHorizontally),
        ) {
            PlaceholderImage(
                url = content.coverImageUrl,
                contentDescription = stringResource(id = R.string.album_detail_cover_content_description),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.height(Dimen.spaceL))
        Text(text = content.albumName, style = AppTheme.typo.head2, color = AppTheme.color.textMain)
        Text(text = content.artistName, style = AppTheme.typo.body1, color = AppTheme.color.textMain)
        Spacer(modifier = Modifier.height(Dimen.spaceL))

        if (content.tags?.isNotEmpty() == true) {
            Tags(content.tags)
            Spacer(modifier = Modifier.height(Dimen.spaceL))
        }

        if (content.tracks?.isNotEmpty() == true) {
            Text(text = stringResource(id = R.string.album_detail_tracks), style = AppTheme.typo.body1, color = AppTheme.color.textMain)
            Spacer(modifier = Modifier.height(Dimen.spaceS))
            Tracks(content.tracks)
        }
    }
}

@Composable
private fun Tags(
    tags: List<Tag>?,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimen.spaceS),
        verticalArrangement = Arrangement.spacedBy(Dimen.spaceS),
    ) {
        tags?.forEach { tag ->
            SuggestionChip(
                onClick = { },
                label = { Text(tag.name) },
            )
        }
    }
}

@Composable
internal fun Tracks(tracks: List<Track>?) {
    tracks?.forEach { track ->
        TrackItem(track)
    }
}

@Composable
internal fun TrackItem(track: Track) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Outlined.Star, contentDescription = null)
        Spacer(modifier = Modifier.width(Dimen.spaceS))

        val text =
            buildString {
                append(track.name)
                track.duration?.let { duration ->
                    append(" ${TimeUtil.formatTime(duration)}")
                }
            }

        Text(text = text, style = AppTheme.typo.head2, color = AppTheme.color.textMain)
    }
}

@Preview
@Composable
private fun TrackItemPreview() {
    AppPreview {
        TrackItem(
            track =
                Track(
                    name = "Sample Track",
                    duration = 180, // 3 minutes in seconds
                ),
        )
    }
}
