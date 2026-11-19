package com.Tofwure2.Kestub.domain.repository

import com.Tofwure2.Kestub.domain.model.GameSettings
import com.Tofwure2.Kestub.domain.model.PlayerProgress

interface ProgressRepository {
    fun load(): PlayerProgress
    fun save(progress: PlayerProgress)
    fun reset()
}

interface SettingsRepository {
    fun load(): GameSettings
    fun save(settings: GameSettings)
}
