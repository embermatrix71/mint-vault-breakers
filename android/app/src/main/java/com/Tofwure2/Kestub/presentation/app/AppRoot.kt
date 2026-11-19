package com.Tofwure2.Kestub.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tofwure2.Kestub.core.di.AppViewModelFactory
import com.Tofwure2.Kestub.core.navigation.Screen
import com.Tofwure2.Kestub.core.theme.BackgroundVoid
import com.Tofwure2.Kestub.presentation.collection.CollectionScreen
import com.Tofwure2.Kestub.presentation.game.GameScreen
import com.Tofwure2.Kestub.presentation.gameover.GameOverScreen
import com.Tofwure2.Kestub.presentation.levelmap.LevelMapScreen
import com.Tofwure2.Kestub.presentation.menu.MenuScreen
import com.Tofwure2.Kestub.presentation.missions.MissionsScreen
import com.Tofwure2.Kestub.presentation.rewards.RewardsScreen
import com.Tofwure2.Kestub.presentation.settings.SettingsScreen
import com.Tofwure2.Kestub.presentation.splash.SplashScreen
import com.Tofwure2.Kestub.presentation.tutorial.TutorialScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundVoid)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                (fadeIn(tween(durationMillis = 220)) + slideInVertically(
                    animationSpec = tween(durationMillis = 260),
                    initialOffsetY = { it / 18 }
                )) togetherWith fadeOut(tween(durationMillis = 180))
            },
            label = "screens"
        ) { current ->
            when (current) {
                Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.openMenuFromSplash() }
                )

                Screen.Menu -> MenuScreen(
                    onPlay = { stage -> appViewModel.startGame(stage) },
                    onOpen = { target -> appViewModel.navigate(target) }
                )

                Screen.Tutorial -> TutorialScreen(
                    onMenu = { appViewModel.toMenu() },
                    onPlay = { stage -> appViewModel.replayGame(stage) }
                )

                is Screen.Game -> GameScreen(
                    stageIndex = current.stageIndex,
                    attempt = current.attempt,
                    onMenu = { appViewModel.toMenu() },
                    onTutorial = { appViewModel.navigate(Screen.Tutorial) },
                    onFinished = { summary -> appViewModel.replace(Screen.GameOver(summary)) }
                )

                is Screen.GameOver -> GameOverScreen(
                    summary = current.summary,
                    onPlayAgain = { stage -> appViewModel.replayGame(stage) },
                    onLevels = { appViewModel.replace(Screen.LevelMap) },
                    onMenu = { appViewModel.toMenu() }
                )

                Screen.LevelMap -> LevelMapScreen(
                    onMenu = { appViewModel.toMenu() },
                    onPlay = { stage -> appViewModel.startGame(stage) }
                )

                Screen.Missions -> MissionsScreen(onMenu = { appViewModel.toMenu() })

                Screen.Rewards -> RewardsScreen(onMenu = { appViewModel.toMenu() })

                Screen.Collection -> CollectionScreen(onMenu = { appViewModel.toMenu() })

                Screen.Settings -> SettingsScreen(onMenu = { appViewModel.toMenu() })
            }
        }
    }
}
