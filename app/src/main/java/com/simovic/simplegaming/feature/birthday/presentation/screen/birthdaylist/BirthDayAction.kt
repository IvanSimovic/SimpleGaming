package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdaylist

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction

internal sealed interface BirthDayAction : BaseAction<BirthDayUiState> {
    object Start : BirthDayAction {
        override fun reduce(state: BirthDayUiState) = BirthDayUiState.Loading
    }

    class Success(
        private val birthdays: List<BirthDayListModel>,
    ) : BirthDayAction {
        override fun reduce(state: BirthDayUiState) = BirthDayUiState.Content(birthdays, false, false)
    }

    class StartDelete(
        private val birthdays: List<BirthDayListModel>,
    ) : BirthDayAction {
        override fun reduce(state: BirthDayUiState) = BirthDayUiState.Content(birthdays, true, false)
    }

    class StopDelete(
        private val birthdays: List<BirthDayListModel>,
    ) : BirthDayAction {
        override fun reduce(state: BirthDayUiState) = BirthDayUiState.Content(birthdays, false, false)
    }

    class UpdateMarkedForDelete(
        private val birthdays: List<BirthDayListModel>,
        private val isSelectedForDelete: Boolean,
    ) : BirthDayAction {
        override fun reduce(state: BirthDayUiState) = BirthDayUiState.Content(birthdays, true, isSelectedForDelete)
    }

    object Fail : BirthDayAction {
        override fun reduce(state: BirthDayUiState) = BirthDayUiState.Error
    }
}
