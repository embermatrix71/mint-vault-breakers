package com.Tofwure2.Kestub.domain.model

enum class PlateId { COPPER, SILVER, GOLD, PRIME }

enum class MissionKind { CRACK_LOCKS, CHAIN_LENGTH, CLEAR_STAGE, BANK_COINS, SILENT_CLEAR, COLLECT_SEALS }

data class Mission(
    val id: Int,
    val titleRes: Int,
    val kind: MissionKind,
    val target: Int,
    val reward: Int,
    val param: Int = 0
)

data class Crate(
    val id: Int,
    val titleRes: Int,
    val threshold: Int,
    val plate: PlateId,
    val plateTitleRes: Int
)

data class Collectible(
    val key: String,
    val titleRes: Int,
    val seal: Boolean,
    val plate: PlateId?
)

data class PlayerProgress(
    val coinsTotal: Int = 0,
    val bestVault: Int = 0,
    val longestChain: Int = 0,
    val locksCracked: Int = 0,
    val stagesUnlocked: Int = 1,
    val stageBest: Map<Int, Int> = emptyMap(),
    val seals: Set<Int> = emptySet(),
    val plates: Set<PlateId> = emptySet(),
    val activePlate: PlateId? = null,
    val cratesClaimed: Set<Int> = emptySet(),
    val missionProgress: Map<Int, Int> = emptyMap(),
    val missionsClaimed: Set<Int> = emptySet(),
    val tutorialSeen: Boolean = false
)

enum class ChainSpeed(val stepMs: Long, val stiffness: Float) {
    RELAXED(220L, 380f),
    STANDARD(140L, 520f),
    SWIFT(80L, 720f)
}

data class GameSettings(
    val chainSpeed: ChainSpeed = ChainSpeed.STANDARD,
    val highContrast: Boolean = false,
    val haptics: Boolean = true,
    val persistentHint: Boolean = false
)
