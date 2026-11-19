package com.Tofwure2.Kestub.domain.usecase

import com.Tofwure2.Kestub.core.config.GameConfig
import com.Tofwure2.Kestub.domain.model.RoundOutcome

class EvaluateRoundUseCase {

    operator fun invoke(coins: Int, quota: Int, strikesLeft: Int, alarm: Int): RoundOutcome? = when {
        coins >= quota -> RoundOutcome.CRACKED
        alarm >= GameConfig.ALARM_MAX -> RoundOutcome.LOCKDOWN
        strikesLeft <= 0 -> RoundOutcome.OUT_OF_STRIKES
        else -> null
    }
}
