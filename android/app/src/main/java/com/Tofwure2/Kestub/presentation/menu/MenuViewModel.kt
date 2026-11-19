package com.Tofwure2.Kestub.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val stage = progress.stagesUnlocked.coerceIn(1, SampleData.stages.size)
            val missionsDone = SampleData.missions.count { mission ->
                (progress.missionProgress[mission.id] ?: 0) >= mission.target
            }
            val cratesReady = SampleData.crates.count { crate ->
                !progress.cratesClaimed.contains(crate.id) && progress.coinsTotal >= crate.threshold
            }
            val owned = progress.seals.size + progress.plates.size
            _state.update {
                it.copy(
                    coins = progress.coinsTotal,
                    bestVault = progress.bestVault,
                    longestChain = progress.longestChain,
                    stagesUnlocked = stage,
                    currentStage = stage,
                    currentStageNameRes = SampleData.stageAt(stage).nameRes,
                    missionsDone = missionsDone,
                    collectionOwned = owned,
                    cratesReady = cratesReady,
                    tutorialSeen = progress.tutorialSeen
                )
            }
        }
    }
}
