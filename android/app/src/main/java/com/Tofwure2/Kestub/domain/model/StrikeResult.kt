package com.Tofwure2.Kestub.domain.model

data class ChainLink(
    val fromIndex: Int,
    val toIndex: Int,
    val linkIndex: Int,
    val coins: Int,
    val seal: Boolean
)

data class StrikeResult(
    val board: VaultBoard,
    val links: List<ChainLink>,
    val dentedIndex: Int,
    val coins: Int,
    val surge: Boolean,
    val sealCracked: Boolean
) {
    val cracks: Int get() = links.size
    val alarmDelta: Int get() = if (links.isEmpty()) 1 else -1
}
