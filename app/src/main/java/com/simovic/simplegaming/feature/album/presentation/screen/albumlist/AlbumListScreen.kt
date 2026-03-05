package com.simovic.simplegaming.feature.album.presentation.screen.albumlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.ErrorAnim
import com.simovic.simplegaming.base.presentation.compose.composable.LoadingIndicator
import com.simovic.simplegaming.base.presentation.compose.composable.PlaceholderImage
import com.simovic.simplegaming.feature.album.domain.model.Album
import com.simovic.simplegaming.feature.album.presentation.composable.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumListScreen(
    modifier: Modifier = Modifier,
    onNavigateToAlbumDetail: ((artistName: String, albumName: String, albumMbId: String?) -> Unit)? = null,
) {
    val viewModel: AlbumListViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            query = "",
            onQueryChange = { newQuery ->
                viewModel.onQueryChanged(newQuery)
            },
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                AlbumListUiState.Loading -> {
                    LoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }

                AlbumListUiState.Error -> {
                    ErrorAnim(modifier = Modifier.align(Alignment.Center))
                }

                is AlbumListUiState.Content -> {
                    AlbumGrid(
                        albums = state.albums,
                        onAlbumClick = { album ->
                            onNavigateToAlbumDetail?.invoke(album.artist, album.name, album.mbId)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumGrid(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(Dimen.imageSize),
        contentPadding = PaddingValues(Dimen.spaceS),
    ) {
        items(
            items = albums,
            key = { it.id },
        ) { album ->
            ElevatedCard(
                modifier =
                    Modifier
                        .padding(Dimen.spaceS)
                        .wrapContentSize(),
                onClick = { onAlbumClick(album) },
            ) {
                PlaceholderImage(
                    url = album.getDefaultImageUrl(),
                    contentDescription = stringResource(R.string.album_detail_cover_content_description),
                    modifier = Modifier.size(Dimen.imageSize),
                )
            }
        }
    }
}
