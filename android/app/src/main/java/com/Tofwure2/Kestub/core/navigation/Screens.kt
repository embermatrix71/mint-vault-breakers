package com.Tofwure2.Kestub.core.navigation

import com.Tofwure2.Kestub.domain.model.RoundSummary

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Tutorial : Screen
    data class Game(val stageIndex: Int, val attempt: Int) : Screen
    data class GameOver(val summary: RoundSummary) : Screen
    data object LevelMap : Screen
    data object Missions : Screen
    data object Rewards : Screen
    data object Collection : Screen
    data object Settings : Screen
}
