package com.simovic.simplegaming.feature.auth.presentation.screen.temp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.simovic.simplegaming.base.common.res.Dimen
import com.simovic.simplegaming.base.presentation.compose.composable.AppButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TempScreen(viewModel: TempViewModel = koinViewModel()) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(Dimen.screenContentPadding),
        contentAlignment = Alignment.Center,
    ) {
        AppButton(
            text = "Sign Out",
            onClick = { viewModel.signOut() },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
