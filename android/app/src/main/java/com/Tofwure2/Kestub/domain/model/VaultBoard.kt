package com.Tofwure2.Kestub.domain.model

data class VaultLock(
    val id: Long,
    val armour: Int,
    val direction: Direction,
    val seal: Boolean = false
)

data class VaultBoard(
    val cols: Int,
    val rows: Int,
    val cells: List<VaultLock?>
) {
    val size: Int get() = cols * rows

    fun indexOf(row: Int, col: Int): Int = row * cols + col

    fun rowOf(index: Int): Int = index / cols

    fun colOf(index: Int): Int = index % cols

    fun lockAt(index: Int): VaultLock? =
        if (index in cells.indices) cells[index] else null

    fun withCells(next: List<VaultLock?>): VaultBoard = copy(cells = next)

    fun nextOccupied(from: Int, direction: Direction): Int {
        var row = rowOf(from) + direction.rowStep
        var col = colOf(from) + direction.colStep
        while (row in 0 until rows && col in 0 until cols) {
            val index = indexOf(row, col)
            if (cells[index] != null) return index
            row += direction.rowStep
            col += direction.colStep
        }
        return -1
    }
}
