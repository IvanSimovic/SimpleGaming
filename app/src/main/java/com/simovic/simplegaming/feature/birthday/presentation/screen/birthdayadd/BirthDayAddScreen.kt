package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdayadd

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anhaki.picktime.PickDayMonth
import com.simovic.simplegaming.R
import com.simovic.simplegaming.base.presentation.compose.composable.AppButton
import com.simovic.simplegaming.base.presentation.compose.composable.AppPreview
import com.simovic.simplegaming.base.presentation.compose.composable.AppTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDayAddScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: BirthDayAddViewModel = koinViewModel()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var day by remember { mutableIntStateOf(1) }
    var month by remember { mutableIntStateOf(1) }
    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.add_birthday)) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = onFinish) {
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
            when (uiState) {
                BirthDayAddUiState.Success -> {
                    onFinish()
                }

                // Navigate back
                is BirthDayAddUiState.Empty -> {
                    BirthDayAddScreen(
                        name,
                        { newName: String -> name = newName },
                        { newDay: Int ->
                            day = newDay
                        },
                        { newMonth: Int ->
                            month = newMonth
                        },
                        {
                            viewModel.add(name, day, month)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun BirthDayAddScreen(
    name: String,
    onNameChange: (String) -> Unit,
    onDayChange: (Int) -> Unit,
    onMonthChange: (Int) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AppTextField(
            value = name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
        )
        Spacer(Modifier.height(16.dp))
        PickDayMonth(
            initialDay = 1,
            onDayChange = onDayChange,
            initialMonth = 1,
            onMonthChange = onMonthChange,
        )
        Spacer(Modifier.height(32.dp))
        AppButton(onClick = { onFinish() }, text = stringResource(R.string.add))
    }
}

@Preview
@Composable
private fun BirthDayAddScreenPreview() {
    AppPreview {
        BirthDayAddScreen(
            name = "John",
            onNameChange = {},
            onDayChange = {},
            onMonthChange = {},
            onFinish = { },
        )
    }
}
