package com.Tofwure2.Kestub.domain.model

enum class Direction(val rowStep: Int, val colStep: Int) {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    fun rotatedClockwise(): Direction = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
}
