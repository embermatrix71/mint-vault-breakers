package com.Tofwure2.Kestub.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.Tofwure2.Kestub.core.theme.GoldAccent
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.ui.PrimaryButton
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.StatCard
import com.Tofwure2.Kestub.core.ui.VaultBackground
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.model.RoundOutcome
import com.Tofwure2.Kestub.domain.model.RoundSummary

@Composable
fun GameOverScreen(
    summary: RoundSummary,
    onPlayAgain: (Int) -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(summary) {
        viewModel.refresh()
    }

    GameOverContent(
        summary = summary,
        state = state,
        onPlayAgain = { onPlayAgain(summary.stageIndex) },
        onLevels = onLevels,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    summary: RoundSummary,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit
) {
    val won = summary.outcome == RoundOutcome.CRACKED
    val accent = when (summary.outcome) {
        RoundOutcome.CRACKED -> MintPrimary
        RoundOutcome.LOCKDOWN -> AlarmRed
        RoundOutcome.OUT_OF_STRIKES -> GoldAccent
    }
    val overlay = when (summary.outcome) {
        RoundOutcome.CRACKED -> listOf(
            Color(0xFF0C1A14).copy(alpha = 0.92f),
            Color(0xFF12241B).copy(alpha = 0.90f)
        )

        RoundOutcome.LOCKDOWN -> listOf(
            Color(0xFF1A0C0E).copy(alpha = 0.92f),
            Color(0xFF241214).copy(alpha = 0.90f)
        )

        RoundOutcome.OUT_OF_STRIKES -> listOf(
            Color(0xFF131318).copy(alpha = 0.92f),
            Color(0xFF1B1B22).copy(alpha = 0.90f)
        )
    }
    var spun by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        spun = true
    }
    val dialTurn by animateFloatAsState(
        targetValue = if (spun) 360f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "dialTurn"
    )

    VaultBackground(
        overlay = Brush.verticalGradient(overlay),
        artAlpha = 0.30f,
        moteCount = 12,
        moteSeed = 41
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(
                    when (summary.outcome) {
                        RoundOutcome.CRACKED -> R.string.result_cracked
                        RoundOutcome.LOCKDOWN -> R.string.result_lockdown
                        RoundOutcome.OUT_OF_STRIKES -> R.string.result_out_of_strikes
                    }
                ),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 28.sp,
                color = accent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (summary.outcome) {
                    RoundOutcome.CRACKED -> stringResource(
                        R.string.verdict_cracked,
                        summary.stageIndex
                    )

                    RoundOutcome.LOCKDOWN -> stringResource(R.string.verdict_lockdown)
                    RoundOutcome.OUT_OF_STRIKES -> stringResource(
                        R.string.verdict_strikes,
                        summary.coins,
                        summary.quota
                    )
                },
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .border(2.dp, accent.copy(alpha = 0.5f), RoundedCornerShape(56.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_dial),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(86.dp)
                        .graphicsLayer { rotationZ = dialTurn }
                )
            }
            if (summary.newBest && summary.coins > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .background(GoldAccent.copy(alpha = 0.16f), RoundedCornerShape(12.dp))
                        .border(1.dp, GoldAccent.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.new_best),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = GoldAccent
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ResultStats(summary = summary)
            MissionToasts(summary = summary)
            Spacer(modifier = Modifier.weight(1f))
            if (won && state.stagesUnlocked > summary.stageIndex) {
                Text(
                    text = stringResource(
                        R.string.menu_stage_line,
                        state.stagesUnlocked,
                        stringResource(SampleData.stageAt(state.stagesUnlocked).nameRes)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MintPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            PrimaryButton(
                text = stringResource(R.string.play_again),
                modifier = Modifier.fillMaxWidth(),
                onClick = onPlayAgain
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.levels),
                    modifier = Modifier.weight(1f),
                    onClick = onLevels
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
private fun ResultStats(summary: RoundSummary) {
    val entries = ArrayList<ResultStat>()
    if (summary.coins > 0) {
        entries.add(
            ResultStat(stringResource(R.string.stat_coins_banked), summary.coins.toString(), GoldAccent)
        )
    }
    if (summary.locksCracked > 0) {
        entries.add(
            ResultStat(
                stringResource(R.string.stat_locks_cracked),
                summary.locksCracked.toString(),
                MintPrimary
            )
        )
    }
    if (summary.longestChain > 0) {
        entries.add(
            ResultStat(
                stringResource(R.string.stat_longest_chain),
                summary.longestChain.toString(),
                TextPrimary
            )
        )
    }
    if (summary.sealFound) {
        entries.add(
            ResultStat(
                stringResource(R.string.stat_mint_seal),
                stringResource(R.string.value_found),
                GoldAccent
            )
        )
    }
    if (entries.size < 2) return
    val rows = if (entries.size == 3) listOf(entries.toList()) else entries.chunked(2)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { stat ->
                    StatCard(
                        label = stat.label,
                        value = stat.value,
                        valueColor = stat.color,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MissionToasts(summary: RoundSummary) {
    if (summary.completedMissions.isEmpty()) return
    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        summary.completedMissions.take(2).forEach { missionId ->
            val mission = SampleData.missions.firstOrNull { it.id == missionId }
            if (mission != null) {
                Text(
                    text = stringResource(
                        R.string.mission_complete,
                        stringResource(mission.titleRes)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MintPrimary
                )
            }
        }
    }
}

private data class ResultStat(
    val label: String,
    val value: String,
    val color: Color
)
