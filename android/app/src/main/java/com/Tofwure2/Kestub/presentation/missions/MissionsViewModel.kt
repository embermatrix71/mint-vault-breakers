package com.Tofwure2.Kestub.presentation.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.model.Mission
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MissionRow(
    val mission: Mission,
    val progress: Int,
    val claimed: Boolean
) {
    val complete: Boolean get() = progress >= mission.target
}

data class MissionsUiState(
    val rows: List<MissionRow> = emptyList(),
    val coinsTotal: Int = 0
) {
    val allClaimed: Boolean get() = rows.isNotEmpty() && rows.all { it.claimed }
}

class MissionsViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(MissionsUiState())
    val state: StateFlow<MissionsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val rows = SampleData.missions.map { mission ->
                MissionRow(
                    mission = mission,
                    progress = (progress.missionProgress[mission.id] ?: 0)
                        .coerceAtMost(mission.target),
                    claimed = progress.missionsClaimed.contains(mission.id)
                )
            }
            _state.update { it.copy(rows = rows, coinsTotal = progress.coinsTotal) }
        }
    }

    fun claim(missionId: Int) {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val mission = SampleData.missions.firstOrNull { it.id == missionId } ?: return@launch
            val value = progress.missionProgress[mission.id] ?: 0
            if (value < mission.target || progress.missionsClaimed.contains(mission.id)) {
                return@launch
            }
            progressRepository.save(
                progress.copy(
                    coinsTotal = progress.coinsTotal + mission.reward,
                    missionsClaimed = progress.missionsClaimed + mission.id
                )
            )
            refresh()
        }
    }
}
