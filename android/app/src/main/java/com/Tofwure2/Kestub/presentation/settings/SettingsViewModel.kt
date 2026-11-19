package com.Tofwure2.Kestub.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.domain.model.ChainSpeed
import com.Tofwure2.Kestub.domain.model.GameSettings
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import com.Tofwure2.Kestub.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: GameSettings = GameSettings(),
    val coinsTotal: Int = 0,
    val stagesUnlocked: Int = 1
)

class SettingsViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val settings = settingsRepository.load()
            val progress = progressRepository.load()
            _state.update {
                it.copy(
                    settings = settings,
                    coinsTotal = progress.coinsTotal,
                    stagesUnlocked = progress.stagesUnlocked
                )
            }
        }
    }

    fun setChainSpeed(speed: ChainSpeed) {
        store(_state.value.settings.copy(chainSpeed = speed))
    }

    fun toggleHighContrast() {
        store(_state.value.settings.copy(highContrast = !_state.value.settings.highContrast))
    }

    fun toggleHaptics() {
        store(_state.value.settings.copy(haptics = !_state.value.settings.haptics))
    }

    fun toggleHint() {
        store(_state.value.settings.copy(persistentHint = !_state.value.settings.persistentHint))
    }

    fun resetProgress() {
        viewModelScope.launch {
            progressRepository.reset()
            refresh()
        }
    }

    private fun store(next: GameSettings) {
        viewModelScope.launch {
            settingsRepository.save(next)
            _state.update { it.copy(settings = next) }
        }
    }
}
