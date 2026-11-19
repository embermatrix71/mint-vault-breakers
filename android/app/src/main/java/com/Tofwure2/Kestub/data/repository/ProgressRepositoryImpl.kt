package com.Tofwure2.Kestub.data.repository

import com.Tofwure2.Kestub.data.local.VaultPreferences
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.model.PlateId
import com.Tofwure2.Kestub.domain.model.PlayerProgress
import com.Tofwure2.Kestub.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val prefs: VaultPreferences) : ProgressRepository {

    override fun load(): PlayerProgress {
        val stageBest = HashMap<Int, Int>()
        val seals = HashSet<Int>()
        for (stage in SampleData.stages) {
            val best = prefs.int("stage_best_${stage.index}", 0)
            if (best > 0) stageBest[stage.index] = best
            if (prefs.bool("seal_${stage.index}", false)) seals.add(stage.index)
        }
        val plates = HashSet<PlateId>()
        for (plate in PlateId.entries) {
            if (prefs.bool("plate_${plate.name.lowercase()}", false)) plates.add(plate)
        }
        val crates = HashSet<Int>()
        for (crate in SampleData.crates) {
            if (prefs.bool("crate_${crate.id}_claimed", false)) crates.add(crate.id)
        }
        val missionProgress = HashMap<Int, Int>()
        val missionsClaimed = HashSet<Int>()
        for (mission in SampleData.missions) {
            val value = prefs.int("mission_${mission.id}_progress", 0)
            if (value > 0) missionProgress[mission.id] = value
            if (prefs.bool("mission_${mission.id}_claimed", false)) missionsClaimed.add(mission.id)
        }
        val activeName = prefs.readString("active_plate", "")
        val active = PlateId.entries.firstOrNull { it.name == activeName }
        return PlayerProgress(
            coinsTotal = prefs.int("coins_total", 0),
            bestVault = prefs.int("best_vault", 0),
            longestChain = prefs.int("longest_chain", 0),
            locksCracked = prefs.int("locks_cracked", 0),
            stagesUnlocked = prefs.int("stages_unlocked", 1).coerceIn(1, SampleData.stages.size),
            stageBest = stageBest,
            seals = seals,
            plates = plates,
            activePlate = active,
            cratesClaimed = crates,
            missionProgress = missionProgress,
            missionsClaimed = missionsClaimed,
            tutorialSeen = prefs.bool("tutorial_seen", false)
        )
    }

    override fun save(progress: PlayerProgress) {
        prefs.putInt("coins_total", progress.coinsTotal)
        prefs.putInt("best_vault", progress.bestVault)
        prefs.putInt("longest_chain", progress.longestChain)
        prefs.putInt("locks_cracked", progress.locksCracked)
        prefs.putInt("stages_unlocked", progress.stagesUnlocked)
        for (stage in SampleData.stages) {
            prefs.putInt("stage_best_${stage.index}", progress.stageBest[stage.index] ?: 0)
            prefs.putBool("seal_${stage.index}", progress.seals.contains(stage.index))
        }
        for (plate in PlateId.entries) {
            prefs.putBool("plate_${plate.name.lowercase()}", progress.plates.contains(plate))
        }
        for (crate in SampleData.crates) {
            prefs.putBool("crate_${crate.id}_claimed", progress.cratesClaimed.contains(crate.id))
        }
        for (mission in SampleData.missions) {
            prefs.putInt("mission_${mission.id}_progress", progress.missionProgress[mission.id] ?: 0)
            prefs.putBool("mission_${mission.id}_claimed", progress.missionsClaimed.contains(mission.id))
        }
        prefs.writeString("active_plate", progress.activePlate?.name ?: "")
        prefs.putBool("tutorial_seen", progress.tutorialSeen)
    }

    override fun reset() {
        save(PlayerProgress())
    }
}
