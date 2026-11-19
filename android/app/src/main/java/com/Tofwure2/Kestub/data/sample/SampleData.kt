package com.Tofwure2.Kestub.data.sample

import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.domain.model.Collectible
import com.Tofwure2.Kestub.domain.model.Crate
import com.Tofwure2.Kestub.domain.model.Mission
import com.Tofwure2.Kestub.domain.model.MissionKind
import com.Tofwure2.Kestub.domain.model.PlateId
import com.Tofwure2.Kestub.domain.model.StageSpec

object SampleData {

    val stages: List<StageSpec> = listOf(
        StageSpec(1, R.string.stage_1, 900, 18, 1),
        StageSpec(2, R.string.stage_2, 1100, 18, 2),
        StageSpec(3, R.string.stage_3, 1300, 17, 3),
        StageSpec(4, R.string.stage_4, 1450, 17, 4),
        StageSpec(5, R.string.stage_5, 1600, 17, 5),
        StageSpec(6, R.string.stage_6, 1750, 17, 6)
    )

    val missions: List<Mission> = listOf(
        Mission(1, R.string.mission_1, MissionKind.CRACK_LOCKS, 50, 250),
        Mission(2, R.string.mission_2, MissionKind.CHAIN_LENGTH, 5, 300),
        Mission(3, R.string.mission_3, MissionKind.CLEAR_STAGE, 1, 400, param = 2),
        Mission(4, R.string.mission_4, MissionKind.BANK_COINS, 5000, 500),
        Mission(5, R.string.mission_5, MissionKind.SILENT_CLEAR, 1, 450),
        Mission(6, R.string.mission_6, MissionKind.COLLECT_SEALS, 3, 600)
    )

    val crates: List<Crate> = listOf(
        Crate(1, R.string.crate_1, 1000, PlateId.COPPER, R.string.plate_copper),
        Crate(2, R.string.crate_2, 3000, PlateId.SILVER, R.string.plate_silver),
        Crate(3, R.string.crate_3, 7000, PlateId.GOLD, R.string.plate_gold),
        Crate(4, R.string.crate_4, 15000, PlateId.PRIME, R.string.plate_prime)
    )

    val collectibles: List<Collectible> = listOf(
        Collectible("seal_1", R.string.seal_1, seal = true, plate = null),
        Collectible("seal_2", R.string.seal_2, seal = true, plate = null),
        Collectible("seal_3", R.string.seal_3, seal = true, plate = null),
        Collectible("seal_4", R.string.seal_4, seal = true, plate = null),
        Collectible("seal_5", R.string.seal_5, seal = true, plate = null),
        Collectible("seal_6", R.string.seal_6, seal = true, plate = null),
        Collectible("plate_copper", R.string.plate_copper, seal = false, plate = PlateId.COPPER),
        Collectible("plate_silver", R.string.plate_silver, seal = false, plate = PlateId.SILVER),
        Collectible("plate_gold", R.string.plate_gold, seal = false, plate = PlateId.GOLD),
        Collectible("plate_prime", R.string.plate_prime, seal = false, plate = PlateId.PRIME)
    )

    fun stageAt(index: Int): StageSpec =
        stages.firstOrNull { it.index == index } ?: stages.first()
}
