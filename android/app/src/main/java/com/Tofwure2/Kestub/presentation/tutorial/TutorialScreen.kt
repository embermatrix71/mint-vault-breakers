package com.Tofwure2.Kestub.presentation.tutorial

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.di.AppViewModelFactory
import com.Tofwure2.Kestub.core.theme.AlarmRed
import com.Tofwure2.Kestub.core.theme.ArmourMedium
import com.Tofwure2.Kestub.core.theme.GoldAccent
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.SurfaceAlt
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.PrimaryButton
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.SectionHeader
import com.Tofwure2.Kestub.core.ui.SegmentMeter
import com.Tofwure2.Kestub.core.ui.VaultBackground

@Composable
fun TutorialScreen(
    onMenu: () -> Unit,
    onPlay: (Int) -> Unit
) {
    val viewModel: TutorialViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    TutorialContent(
        state = state,
        onNext = { viewModel.next() },
        onPrevious = { viewModel.previous() },
        onMenu = onMenu,
        onPlay = {
            viewModel.markSeen()
            viewModel.restart()
            onPlay(state.startStage)
        }
    )
}

@Composable
private fun TutorialContent(
    state: TutorialUiState,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onMenu: () -> Unit,
    onPlay: () -> Unit
) {
    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF0E0E13).copy(alpha = 0.90f),
                Color(0xFF141A19).copy(alpha = 0.86f),
                Color(0xFF0B0B10).copy(alpha = 0.94f)
            )
        ),
        artAlpha = 0.25f,
        moteCount = 10,
        moteSeed = 21
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SectionHeader(title = stringResource(R.string.tutorial), onHome = onMenu)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    R.string.tutorial_step_counter,
                    state.step + 1,
                    state.totalSteps
                ),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = MintPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedContent(
                targetState = state.step,
                transitionSpec = {
                    fadeIn(tween(durationMillis = 200)) togetherWith
                        fadeOut(tween(durationMillis = 160))
                },
                label = "tutorialStep",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { step ->
                TutorialPanel(step = step)
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (state.step == state.totalSteps - 1) {
                PrimaryButton(
                    text = stringResource(R.string.tutorial_cta_last),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPlay
                )
            } else {
                PrimaryButton(
                    text = stringResource(R.string.next),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNext
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.back),
                    enabled = state.step > 0,
                    modifier = Modifier.weight(1f),
                    onClick = onPrevious
                )
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    modifier = Modifier.weight(1f),
                    onClick = onMenu
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TutorialPanel(step: Int) {
    val titleRes = when (step) {
        0 -> R.string.tutorial_title_1
        1 -> R.string.tutorial_title_2
        2 -> R.string.tutorial_title_3
        else -> R.string.tutorial_title_4
    }
    val bodyRes = when (step) {
        0 -> R.string.tutorial_body_1
        1 -> R.string.tutorial_body_2
        2 -> R.string.tutorial_body_3
        else -> R.string.tutorial_body_4
    }
    GlassPanel(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (step) {
                    0 -> PlateDemo()
                    1 -> ChainDemo()
                    2 -> AlarmDemo()
                    else -> PryDemo()
                }
            }
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp,
                color = MintPrimary
            )
            Text(
                text = stringResource(bodyRes),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PlateDemo() {
    val transition = rememberInfiniteTransition(label = "plateDemo")
    val shrink by transition.animateFloat(
        initialValue = 3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "plates"
    )
    val plates = shrink.toInt().coerceIn(1, 3)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (index in 0 until plates) {
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 5.dp)
                        .background(TextPrimary, RoundedCornerShape(2.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(R.drawable.sprite_1),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(84.dp)
        )
    }
}

@Composable
private fun ChainDemo() {
    val transition = rememberInfiniteTransition(label = "chainDemo")
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until 3) {
            val active = sweep >= index
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        if (active) MintPrimary.copy(alpha = 0.22f) else ArmourMedium.copy(alpha = 0.5f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                        .graphicsLayer { alpha = if (active) 0.4f else 1f }
                )
            }
        }
    }
}

@Composable
private fun AlarmDemo() {
    val transition = rememberInfiniteTransition(label = "alarmDemo")
    val level by transition.animateFloat(
        initialValue = 0f,
        targetValue = 3.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alarmLevel"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = stringResource(R.string.cd_dial),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(74.dp)
                .graphicsLayer { rotationZ = level * 24f }
        )
        Spacer(modifier = Modifier.height(14.dp))
        SegmentMeter(
            filled = level.toInt().coerceIn(0, 3),
            total = 3,
            fill = AlarmRed,
            description = stringResource(R.string.state_alarm, level.toInt().coerceIn(0, 3))
        )
    }
}

@Composable
private fun PryDemo() {
    val transition = rememberInfiniteTransition(label = "pryDemo")
    val turn by transition.animateFloat(
        initialValue = 0f,
        targetValue = 270f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800),
            repeatMode = RepeatMode.Restart
        ),
        label = "turn"
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_3),
            contentDescription = stringResource(R.string.cd_pry_tool),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(62.dp)
        )
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(SurfaceAlt, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_dir),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(26.dp)
                    .graphicsLayer { rotationZ = turn }
            )
        }
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(GoldAccent, CircleShape)
        )
    }
}
