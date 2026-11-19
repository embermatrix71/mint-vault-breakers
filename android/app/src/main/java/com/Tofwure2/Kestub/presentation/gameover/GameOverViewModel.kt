package com.Tofwure2.Kestub.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val coinsTotal: Int = 0,
    val bestVault: Int = 0,
    val stagesUnlocked: Int = 1
)

class GameOverViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update {
                it.copy(
                    coinsTotal = progress.coinsTotal,
                    bestVault = progress.bestVault,
                    stagesUnlocked = progress.stagesUnlocked
                )
            }
        }
    }
}
