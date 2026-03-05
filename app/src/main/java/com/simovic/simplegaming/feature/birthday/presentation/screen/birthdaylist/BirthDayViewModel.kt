package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdaylist

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.birthday.domain.usecase.DeleteBirthdaysUseCase
import com.simovic.simplegaming.feature.birthday.domain.usecase.GetBirthdaysUseCase
import kotlinx.coroutines.launch

internal class BirthDayViewModel(
    private val getBirthdaysUseCase: GetBirthdaysUseCase,
    private val deleteBirthdaysUseCase: DeleteBirthdaysUseCase,
) : BaseViewModel<BirthDayUiState, BirthDayAction>(BirthDayUiState.Loading) {
    init {
        viewModelScope.launch {
            when (val result = getBirthdaysUseCase()) {
                is Result.Success -> sendAction(BirthDayAction.Success(result.value.toBirthDayListModel()))
                is Result.Failure -> sendAction(BirthDayAction.Fail)
            }
        }
    }

    fun selectForDelete(id: Long) {
        val state = uiStateFlow.value as? BirthDayUiState.Content ?: return
        val updatedList = state.birthdays.map { if (id == it.id) it.copy(isMarkedForDelete = !it.isMarkedForDelete) else it }
        val isMarkedForDelete = updatedList.firstOrNull { it.isMarkedForDelete } != null
        sendAction(BirthDayAction.UpdateMarkedForDelete(updatedList, isMarkedForDelete))
    }

    fun delete() {
        val state = uiStateFlow.value as? BirthDayUiState.Content ?: return
        if (!state.isDeleteModeActive) {
            sendAction(BirthDayAction.StartDelete(state.birthdays))
        } else if (!state.isSelectedForDelete) {
            sendAction(BirthDayAction.StopDelete(state.birthdays))
        } else {
            viewModelScope.launch {
                val ids = state.birthdays.filter { it.isMarkedForDelete }.map { it.id }
                deleteBirthdaysUseCase(ids)
                sendAction(BirthDayAction.StopDelete(state.birthdays.filter { !it.isMarkedForDelete }))
            }
        }
    }
}
