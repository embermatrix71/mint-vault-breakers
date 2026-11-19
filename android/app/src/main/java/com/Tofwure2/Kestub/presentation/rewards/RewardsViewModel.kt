package com.Tofwure2.Kestub.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.model.Crate
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CrateRow(
    val crate: Crate,
    val claimed: Boolean,
    val ready: Boolean,
    val missing: Int
)

data class RewardsUiState(
    val rows: List<CrateRow> = emptyList(),
    val coinsTotal: Int = 0
) {
    val anyReady: Boolean get() = rows.any { it.ready }
}

class RewardsViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val rows = SampleData.crates.map { crate ->
                val claimed = progress.cratesClaimed.contains(crate.id)
                CrateRow(
                    crate = crate,
                    claimed = claimed,
                    ready = !claimed && progress.coinsTotal >= crate.threshold,
                    missing = (crate.threshold - progress.coinsTotal).coerceAtLeast(0)
                )
            }
            _state.update { it.copy(rows = rows, coinsTotal = progress.coinsTotal) }
        }
    }

    fun claim(crateId: Int) {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val crate = SampleData.crates.firstOrNull { it.id == crateId } ?: return@launch
            if (progress.cratesClaimed.contains(crate.id) || progress.coinsTotal < crate.threshold) {
                return@launch
            }
            progressRepository.save(
                progress.copy(
                    cratesClaimed = progress.cratesClaimed + crate.id,
                    plates = progress.plates + crate.plate,
                    activePlate = progress.activePlate ?: crate.plate
                )
            )
            refresh()
        }
    }
}
