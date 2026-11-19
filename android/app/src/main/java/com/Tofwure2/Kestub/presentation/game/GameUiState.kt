package com.Tofwure2.Kestub.presentation.game

import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.domain.model.Direction
import com.Tofwure2.Kestub.domain.model.PlateId
import com.Tofwure2.Kestub.domain.model.RoundSummary

data class LockCell(
    val id: Long,
    val armour: Int,
    val direction: Direction?,
    val seal: Boolean
)

data class GameUiState(
    val stageIndex: Int = 1,
    val stageNameRes: Int = R.string.stage_1,
    val quota: Int = 900,
    val coins: Int = 0,
    val strikesLeft: Int = 18,
    val alarm: Int = 0,
    val cols: Int = 5,
    val rows: Int = 6,
    val cells: List<LockCell> = emptyList(),
    val busy: Boolean = false,
    val paused: Boolean = false,
    val finished: Boolean = false,
    val pryCharges: Int = 0,
    val pryArmed: Boolean = false,
    val chainActive: Set<Int> = emptySet(),
    val strikesMade: Int = 0,
    val persistentHint: Boolean = false,
    val highContrast: Boolean = false,
    val activePlate: PlateId? = null,
    val surgeShown: Boolean = false,
    val longestChain: Int = 0,
    val locksCracked: Int = 0
) {
    val hintVisible: Boolean get() = persistentHint || strikesMade < 3
    val quotaProgress: Float get() = if (quota <= 0) 0f else coins.toFloat() / quota.toFloat()
}

sealed interface GameEvent {
    data class Finished(val summary: RoundSummary) : GameEvent
    data class Pulse(val strong: Boolean) : GameEvent
}
