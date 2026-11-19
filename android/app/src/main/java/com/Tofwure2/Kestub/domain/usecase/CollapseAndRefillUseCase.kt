package com.Tofwure2.Kestub.domain.usecase

import com.Tofwure2.Kestub.domain.model.IdSource
import com.Tofwure2.Kestub.domain.model.VaultBoard
import com.Tofwure2.Kestub.domain.model.VaultLock
import kotlin.random.Random

class CollapseAndRefillUseCase(private val generator: GenerateBoardUseCase) {

    operator fun invoke(board: VaultBoard, random: Random, ids: IdSource): VaultBoard {
        val cells = board.cells.toMutableList()
        for (col in 0 until board.cols) {
            val stack = ArrayList<VaultLock>()
            for (row in board.rows - 1 downTo 0) {
                val lock = cells[board.indexOf(row, col)]
                if (lock != null) stack.add(lock)
            }
            for (row in board.rows - 1 downTo 0) {
                val slot = board.rows - 1 - row
                cells[board.indexOf(row, col)] =
                    if (slot < stack.size) stack[slot] else generator.randomLock(random, ids)
            }
        }
        return board.withCells(cells)
    }
}
