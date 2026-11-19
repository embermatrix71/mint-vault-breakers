package com.Tofwure2.Kestub.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.model.Collectible
import com.Tofwure2.Kestub.domain.model.PlateId
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CollectibleRow(
    val item: Collectible,
    val owned: Boolean,
    val active: Boolean
)

data class CollectionUiState(
    val rows: List<CollectibleRow> = emptyList()
) {
    val ownedCount: Int get() = rows.count { it.owned }
}

class CollectionViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val rows = SampleData.collectibles.mapIndexed { index, item ->
                val owned = if (item.seal) {
                    progress.seals.contains(index + 1)
                } else {
                    item.plate != null && progress.plates.contains(item.plate)
                }
                CollectibleRow(
                    item = item,
                    owned = owned,
                    active = item.plate != null && item.plate == progress.activePlate
                )
            }
            _state.update { it.copy(rows = rows) }
        }
    }

    fun selectPlate(plate: PlateId) {
        viewModelScope.launch {
            val progress = progressRepository.load()
            if (!progress.plates.contains(plate)) return@launch
            progressRepository.save(progress.copy(activePlate = plate))
            refresh()
        }
    }
}
