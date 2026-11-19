package com.Tofwure2.Kestub.domain.model

class IdSource(private var next: Long = 1L) {
    fun take(): Long {
        val value = next
        next += 1L
        return value
    }
}
