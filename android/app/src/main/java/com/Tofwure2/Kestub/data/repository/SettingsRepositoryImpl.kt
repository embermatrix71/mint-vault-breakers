package com.Tofwure2.Kestub.data.repository

import com.Tofwure2.Kestub.data.local.VaultPreferences
import com.Tofwure2.Kestub.domain.model.ChainSpeed
import com.Tofwure2.Kestub.domain.model.GameSettings
import com.Tofwure2.Kestub.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val prefs: VaultPreferences) : SettingsRepository {

    override fun load(): GameSettings {
        val speedName = prefs.readString("set_chain_speed", ChainSpeed.STANDARD.name)
        val speed = ChainSpeed.entries.firstOrNull { it.name == speedName } ?: ChainSpeed.STANDARD
        return GameSettings(
            chainSpeed = speed,
            highContrast = prefs.bool("set_high_contrast", false),
            haptics = prefs.bool("set_haptics", true),
            persistentHint = prefs.bool("set_chain_hint", false)
        )
    }

    override fun save(settings: GameSettings) {
        prefs.writeString("set_chain_speed", settings.chainSpeed.name)
        prefs.putBool("set_high_contrast", settings.highContrast)
        prefs.putBool("set_haptics", settings.haptics)
        prefs.putBool("set_chain_hint", settings.persistentHint)
    }
}
