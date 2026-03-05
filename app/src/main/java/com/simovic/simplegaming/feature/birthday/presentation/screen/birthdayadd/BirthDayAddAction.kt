package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdayadd

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction

internal sealed interface BirthDayAddAction : BaseAction<BirthDayAddUiState> {
    class Success : BirthDayAddAction {
        override fun reduce(state: BirthDayAddUiState) = BirthDayAddUiState.Success
    }
}
