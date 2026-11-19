package com.Tofwure2.Kestub.presentation.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TutorialUiState(
    val step: Int = 0,
    val totalSteps: Int = 4,
    val startStage: Int = 1
)

class TutorialViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(TutorialUiState())
    val state: StateFlow<TutorialUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update { it.copy(startStage = progress.stagesUnlocked.coerceAtLeast(1)) }
        }
    }

    fun restart() {
        _state.update { it.copy(step = 0) }
    }

    fun next() {
        _state.update { it.copy(step = (it.step + 1).coerceAtMost(it.totalSteps - 1)) }
    }

    fun previous() {
        _state.update { it.copy(step = (it.step - 1).coerceAtLeast(0)) }
    }

    fun markSeen() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            progressRepository.save(progress.copy(tutorialSeen = true))
        }
    }
}
