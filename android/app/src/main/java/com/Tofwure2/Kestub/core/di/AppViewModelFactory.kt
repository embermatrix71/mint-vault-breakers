package com.Tofwure2.Kestub.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Tofwure2.Kestub.presentation.app.AppViewModel
import com.Tofwure2.Kestub.presentation.collection.CollectionViewModel
import com.Tofwure2.Kestub.presentation.game.GameViewModel
import com.Tofwure2.Kestub.presentation.gameover.GameOverViewModel
import com.Tofwure2.Kestub.presentation.levelmap.LevelMapViewModel
import com.Tofwure2.Kestub.presentation.menu.MenuViewModel
import com.Tofwure2.Kestub.presentation.missions.MissionsViewModel
import com.Tofwure2.Kestub.presentation.rewards.RewardsViewModel
import com.Tofwure2.Kestub.presentation.settings.SettingsViewModel
import com.Tofwure2.Kestub.presentation.splash.SplashViewModel
import com.Tofwure2.Kestub.presentation.tutorial.TutorialViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(ServiceLocator.progressRepository())

            modelClass.isAssignableFrom(TutorialViewModel::class.java) ->
                TutorialViewModel(ServiceLocator.progressRepository())

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    ServiceLocator.progressRepository(),
                    ServiceLocator.settingsRepository(),
                    ServiceLocator.generateBoard,
                    ServiceLocator.resolveStrike,
                    ServiceLocator.collapseAndRefill,
                    ServiceLocator.evaluateRound,
                    ServiceLocator.updateMissions
                )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(ServiceLocator.progressRepository())

            modelClass.isAssignableFrom(LevelMapViewModel::class.java) ->
                LevelMapViewModel(ServiceLocator.progressRepository())

            modelClass.isAssignableFrom(MissionsViewModel::class.java) ->
                MissionsViewModel(ServiceLocator.progressRepository())

            modelClass.isAssignableFrom(RewardsViewModel::class.java) ->
                RewardsViewModel(ServiceLocator.progressRepository())

            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(ServiceLocator.progressRepository())

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(
                    ServiceLocator.progressRepository(),
                    ServiceLocator.settingsRepository()
                )

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
