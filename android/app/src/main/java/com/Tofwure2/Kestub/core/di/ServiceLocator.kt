package com.Tofwure2.Kestub.core.di

import android.content.Context
import com.Tofwure2.Kestub.data.local.VaultPreferences
import com.Tofwure2.Kestub.data.repository.ProgressRepositoryImpl
import com.Tofwure2.Kestub.data.repository.SettingsRepositoryImpl
import com.Tofwure2.Kestub.domain.repository.ProgressRepository
import com.Tofwure2.Kestub.domain.repository.SettingsRepository
import com.Tofwure2.Kestub.domain.usecase.CollapseAndRefillUseCase
import com.Tofwure2.Kestub.domain.usecase.EvaluateRoundUseCase
import com.Tofwure2.Kestub.domain.usecase.GenerateBoardUseCase
import com.Tofwure2.Kestub.domain.usecase.ResolveStrikeUseCase
import com.Tofwure2.Kestub.domain.usecase.UpdateMissionsUseCase

object ServiceLocator {

    private var preferences: VaultPreferences? = null
    private var progress: ProgressRepository? = null
    private var settings: SettingsRepository? = null

    val generateBoard: GenerateBoardUseCase = GenerateBoardUseCase()
    val resolveStrike: ResolveStrikeUseCase = ResolveStrikeUseCase()
    val collapseAndRefill: CollapseAndRefillUseCase = CollapseAndRefillUseCase(generateBoard)
    val evaluateRound: EvaluateRoundUseCase = EvaluateRoundUseCase()
    val updateMissions: UpdateMissionsUseCase = UpdateMissionsUseCase()

    fun init(context: Context) {
        if (preferences == null) {
            val created = VaultPreferences(context)
            preferences = created
            progress = ProgressRepositoryImpl(created)
            settings = SettingsRepositoryImpl(created)
        }
    }

    fun progressRepository(): ProgressRepository =
        checkNotNull(progress) { "ServiceLocator.init was not called" }

    fun settingsRepository(): SettingsRepository =
        checkNotNull(settings) { "ServiceLocator.init was not called" }
}
