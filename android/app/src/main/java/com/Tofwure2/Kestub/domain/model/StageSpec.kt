package com.Tofwure2.Kestub.domain.model

data class StageSpec(
    val index: Int,
    val nameRes: Int,
    val quota: Int,
    val strikes: Int,
    val sealId: Int
)

enum class RoundOutcome { CRACKED, LOCKDOWN, OUT_OF_STRIKES }

data class RoundSummary(
    val stageIndex: Int,
    val outcome: RoundOutcome,
    val coins: Int,
    val quota: Int,
    val locksCracked: Int,
    val longestChain: Int,
    val sealFound: Boolean,
    val silentAlarm: Boolean,
    val newBest: Boolean,
    val completedMissions: List<Int>
)
