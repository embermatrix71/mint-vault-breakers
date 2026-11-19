package com.Tofwure2.Kestub.presentation.levelmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LevelMapUiState(
    val stagesUnlocked: Int = 1,
    val stageBest: Map<Int, Int> = emptyMap(),
    val seals: Set<Int> = emptySet()
)

class LevelMapViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(LevelMapUiState())
    val state: StateFlow<LevelMapUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update {
                it.copy(
                    stagesUnlocked = progress.stagesUnlocked,
                    stageBest = progress.stageBest,
                    seals = progress.seals
                )
            }
        }
    }
}
