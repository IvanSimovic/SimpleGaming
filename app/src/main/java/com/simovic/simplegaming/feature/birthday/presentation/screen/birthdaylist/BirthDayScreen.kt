package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdaylist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppCheckbox
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.ErrorAnim
import com.simovic.simplegaming.base.presentation.compose.composable.LoadingIndicator
import com.simovic.simplegaming.base.presentation.ui.AppTheme
import com.simovic.simplegaming.base.util.toBirthDay
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun ToolBarDelete(
    isDeleteModeActive: Boolean,
    isSelectedForDelete: Boolean,
    confirmDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (icon, descriptionRes) =
        when {
            isDeleteModeActive && isSelectedForDelete -> Icons.Filled.Done to R.string.finish_delete
            isDeleteModeActive -> Icons.Filled.Close to R.string.cancel_delete
            else -> Icons.Filled.Delete to R.string.start_delete
        }

    IconButton(modifier = modifier, onClick = confirmDelete) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(descriptionRes),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDayScreen(modifier: Modifier = Modifier) {
    val viewModel: BirthDayViewModel = koinViewModel()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.birthdays)) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            actions = {
                (uiState as? BirthDayUiState.Content)?.let { state ->
                    ToolBarDelete(state.isDeleteModeActive, state.isSelectedForDelete, viewModel::delete)
                }
            },
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (val currentUiState = uiState) {
                BirthDayUiState.Error -> ErrorAnim()
                BirthDayUiState.Loading -> LoadingIndicator()
                is BirthDayUiState.Content -> Content(currentUiState, viewModel::selectForDelete)
            }
        }
    }
}

@Composable
private fun Content(
    uiState: BirthDayUiState.Content,
    onMarkedForDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier.padding(horizontal = Dimen.screenContentPadding)) {
        itemsIndexed(uiState.birthdays, key = { _, item -> item.id }) { index, item ->
            Column {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            item.name,
                            style = AppTheme.typo.head5,
                            color = AppTheme.color.textMain,
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            item.date,
                            style = AppTheme.typo.head2,
                            color = AppTheme.color.textMain,
                        )
                    }
                    if (uiState.isDeleteModeActive) {
                        Spacer(Modifier.width(20.dp))
                        AppCheckbox(
                            checked = item.isMarkedForDelete,
                            onCheckedChange = { onMarkedForDelete(item.id) },
                        )
                    }
                }

                if (index < uiState.birthdays.lastIndex) {
                    HorizontalDivider(
                        color = AppTheme.color.divider,
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FeedGridPreview() {
    val sampleData =
        listOf(
            BirthDayListModel(
                id = 1,
                name = "Johny",
                date = LocalDate.now().toBirthDay(),
                isMarkedForDelete = false,
            ),
            BirthDayListModel(
                id = 2,
                name = "Bravo",
                date = LocalDate.now().toBirthDay(),
                isMarkedForDelete = false,
            ),
        )

    AppPreview {
        Content(
            uiState = BirthDayUiState.Content(sampleData, isDeleteModeActive = false, isSelectedForDelete = false),
            onMarkedForDelete = {},
        )
    }
}
