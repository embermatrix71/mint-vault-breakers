package com.Tofwure2.Kestub.domain.usecase

import com.Tofwure2.Kestub.core.config.GameConfig
import com.Tofwure2.Kestub.domain.model.ChainLink
import com.Tofwure2.Kestub.domain.model.StrikeResult
import com.Tofwure2.Kestub.domain.model.VaultBoard
import com.Tofwure2.Kestub.domain.model.VaultLock
import kotlin.math.min
import kotlin.math.roundToInt

class ResolveStrikeUseCase {

    operator fun invoke(board: VaultBoard, index: Int): StrikeResult {
        val cells = board.cells.toMutableList()
        val links = ArrayList<ChainLink>()
        var coins = 0
        var sealCracked = false
        var dented = -1
        var from = -1
        var current = index
        var depth = 0

        while (current >= 0 && depth < GameConfig.MAX_CHAIN_DEPTH) {
            val lock: VaultLock = cells[current] ?: break
            if (lock.armour > 1) {
                cells[current] = lock.copy(armour = lock.armour - 1)
                dented = current
                break
            }
            depth += 1
            var pay = (GameConfig.BASE_PAYOUT * linkMultiplier(depth)).roundToInt()
            if (lock.seal) {
                pay += GameConfig.SEAL_PAYOUT
                sealCracked = true
            }
            coins += pay
            cells[current] = null
            links.add(ChainLink(from, current, depth, pay, lock.seal))
            from = current
            current = board.withCells(cells).nextOccupied(current, lock.direction)
        }

        val surge = links.size >= GameConfig.SURGE_CHAIN
        if (surge) {
            coins += GameConfig.SURGE_BONUS
        }
        return StrikeResult(
            board = board.withCells(cells),
            links = links,
            dentedIndex = dented,
            coins = coins,
            surge = surge,
            sealCracked = sealCracked
        )
    }

    fun linkMultiplier(link: Int): Float = min(1f + 0.5f * (link - 1), 4f)
}
