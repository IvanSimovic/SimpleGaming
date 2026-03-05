package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdayadd

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.birthday.domain.model.CreateBirthDay
import com.simovic.simplegaming.feature.birthday.domain.usecase.AddBirthdayUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate

internal class BirthDayAddViewModel(
    private val addBirthdayUseCase: AddBirthdayUseCase,
) : BaseViewModel<BirthDayAddUiState, BirthDayAddAction>(BirthDayAddUiState.Empty) {
    fun add(
        name: String,
        dayOfMonth: Int,
        month: Int,
    ) {
        viewModelScope.launch {
            if (name.isNotEmpty()) {
                addBirthdayUseCase(CreateBirthDay(name, LocalDate.of(2000, month, dayOfMonth)))
                sendAction(BirthDayAddAction.Success())
            }
        }
    }
}
