package com.Tofwure2.Kestub.presentation.menu

import com.Tofwure2.Kestub.R

data class MenuUiState(
    val coins: Int = 0,
    val bestVault: Int = 0,
    val longestChain: Int = 0,
    val stagesUnlocked: Int = 1,
    val currentStage: Int = 1,
    val currentStageNameRes: Int = R.string.stage_1,
    val missionsDone: Int = 0,
    val collectionOwned: Int = 0,
    val cratesReady: Int = 0,
    val tutorialSeen: Boolean = false
)
