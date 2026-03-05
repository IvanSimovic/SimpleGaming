package com.simovic.simplegaming.base.presentation.viewmodel

interface BaseAction<State> {
    fun reduce(state: State): State
}
