package com.Tofwure2.Kestub.domain.usecase

import com.Tofwure2.Kestub.domain.model.Mission
import com.Tofwure2.Kestub.domain.model.MissionKind
import com.Tofwure2.Kestub.domain.model.PlayerProgress
import com.Tofwure2.Kestub.domain.model.RoundOutcome
import kotlin.math.max
import kotlin.math.min

data class MissionUpdate(
    val progress: PlayerProgress,
    val completed: List<Int>
)

class UpdateMissionsUseCase {

    operator fun invoke(
        progress: PlayerProgress,
        missions: List<Mission>,
        stageIndex: Int,
        outcome: RoundOutcome,
        longestChain: Int,
        silentAlarm: Boolean
    ): MissionUpdate {
        val updated = HashMap(progress.missionProgress)
        val completed = ArrayList<Int>()
        for (mission in missions) {
            val before = updated[mission.id] ?: 0
            val value = when (mission.kind) {
                MissionKind.CRACK_LOCKS -> progress.locksCracked
                MissionKind.CHAIN_LENGTH -> max(before, longestChain)
                MissionKind.BANK_COINS -> progress.coinsTotal
                MissionKind.COLLECT_SEALS -> progress.seals.size
                MissionKind.CLEAR_STAGE ->
                    if (outcome == RoundOutcome.CRACKED && stageIndex == mission.param) 1 else before
                MissionKind.SILENT_CLEAR ->
                    if (outcome == RoundOutcome.CRACKED && silentAlarm) 1 else before
            }
            val capped = min(value, mission.target)
            updated[mission.id] = capped
            if (before < mission.target && capped >= mission.target) {
                completed.add(mission.id)
            }
        }
        return MissionUpdate(progress.copy(missionProgress = updated), completed)
    }
}
