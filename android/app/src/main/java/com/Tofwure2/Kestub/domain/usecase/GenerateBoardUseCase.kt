package com.Tofwure2.Kestub.domain.usecase

import com.Tofwure2.Kestub.core.config.GameConfig
import com.Tofwure2.Kestub.domain.model.Direction
import com.Tofwure2.Kestub.domain.model.IdSource
import com.Tofwure2.Kestub.domain.model.VaultBoard
import com.Tofwure2.Kestub.domain.model.VaultLock
import kotlin.random.Random

data class GeneratedBoard(
    val board: VaultBoard,
    val seed: Long,
    val random: Random,
    val fallback: Boolean
)

class GenerateBoardUseCase {

    operator fun invoke(stageIndex: Int, attempt: Int, ids: IdSource): GeneratedBoard {
        val seed = stageIndex * 7919L xor (attempt * 104729L)
        val random = Random(seed)
        var best: VaultBoard? = null
        var tries = 0
        while (tries < GameConfig.GENERATION_ATTEMPTS) {
            tries += 1
            val candidate = build(random, ids)
            if (isSolvable(candidate)) {
                return GeneratedBoard(candidate, seed, random, false)
            }
            if (best == null || weakCount(candidate) > weakCount(best)) {
                best = candidate
            }
        }
        val graded = best
        if (graded != null && weakCount(graded) >= 2) {
            return GeneratedBoard(graded, seed, random, false)
        }
        return GeneratedBoard(fallbackBoard(ids), seed, random, true)
    }

    fun randomLock(random: Random, ids: IdSource): VaultLock =
        VaultLock(
            id = ids.take(),
            armour = rollArmour(random),
            direction = Direction.entries[random.nextInt(Direction.entries.size)]
        )

    private fun build(random: Random, ids: IdSource): VaultBoard {
        val size = GameConfig.COLS * GameConfig.ROWS
        val cells = ArrayList<VaultLock?>(size)
        for (i in 0 until size) {
            cells.add(randomLock(random, ids))
        }
        val sealIndex = random.nextInt(size)
        val base = cells[sealIndex]
        if (base != null) {
            cells[sealIndex] = base.copy(armour = 3, seal = true)
        }
        return VaultBoard(GameConfig.COLS, GameConfig.ROWS, cells)
    }

    private fun rollArmour(random: Random): Int {
        val roll = random.nextInt(100)
        return when {
            roll < 35 -> 1
            roll < 75 -> 2
            else -> 3
        }
    }

    private fun weakCount(board: VaultBoard): Int =
        board.cells.count { it != null && it.armour == 1 && !it.seal }

    fun isSolvable(board: VaultBoard): Boolean {
        if (weakCount(board) < GameConfig.MIN_WEAK_LOCKS) return false
        for (index in board.cells.indices) {
            val lock = board.cells[index] ?: continue
            if (lock.armour != 1 || lock.seal) continue
            val next = board.nextOccupied(index, lock.direction)
            if (next < 0) continue
            val target = board.cells[next] ?: continue
            if (target.armour == 1) return true
        }
        return false
    }

    private fun fallbackBoard(ids: IdSource): VaultBoard {
        val armour = intArrayOf(
            1, 1, 2, 3, 2,
            2, 1, 1, 2, 3,
            3, 2, 1, 1, 2,
            2, 3, 2, 1, 1,
            1, 2, 3, 2, 1,
            1, 1, 2, 3, 2
        )
        val dirs = arrayOf(
            Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.LEFT,
            Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.UP, Direction.LEFT,
            Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.LEFT,
            Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN,
            Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.UP, Direction.LEFT,
            Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.UP, Direction.LEFT
        )
        val cells = ArrayList<VaultLock?>(armour.size)
        for (i in armour.indices) {
            cells.add(VaultLock(ids.take(), armour[i], dirs[i], seal = i == 14))
        }
        val sealed = cells[14]
        if (sealed != null) {
            cells[14] = sealed.copy(armour = 3, seal = true)
        }
        return VaultBoard(GameConfig.COLS, GameConfig.ROWS, cells)
    }
}
