package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdaylist

import androidx.compose.runtime.Immutable
import com.simovic.simplegaming.base.presentation.viewmodel.BaseState

@Immutable
internal sealed interface BirthDayUiState : BaseState {
    @Immutable
    data class Content(
        val birthdays: List<BirthDayListModel>,
        val isDeleteModeActive: Boolean,
        val isSelectedForDelete: Boolean,
    ) : BirthDayUiState

    @Immutable
    data object Loading : BirthDayUiState

    @Immutable
    data object Error : BirthDayUiState
}
