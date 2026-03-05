package com.simovic.simplegaming.base.presentation

import androidx.lifecycle.ViewModel
import com.simovic.simplegaming.base.data.FabConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScaffoldController : ViewModel() {
    private val _fabConfig = MutableStateFlow(FabConfig(isVisible = false))
    val fabConfig: StateFlow<FabConfig> = _fabConfig.asStateFlow()

    fun setFabConfig(config: FabConfig) {
        _fabConfig.value = config
    }

    fun clearFabConfig() {
        _fabConfig.value = FabConfig(isVisible = false)
    }
}
