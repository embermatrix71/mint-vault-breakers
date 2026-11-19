package com.Tofwure2.Kestub.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.di.AppViewModelFactory
import com.Tofwure2.Kestub.core.theme.AlarmRed
import com.Tofwure2.Kestub.core.theme.GoldAccent
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.PlateCopper
import com.Tofwure2.Kestub.core.theme.PlateGold
import com.Tofwure2.Kestub.core.theme.PlatePrime
import com.Tofwure2.Kestub.core.theme.PlateSilver
import com.Tofwure2.Kestub.core.theme.SurfaceDeepPlate
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.ui.BoardFrameBrackets
import com.Tofwure2.Kestub.core.ui.CoinValue
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.MeterBar
import com.Tofwure2.Kestub.core.ui.PrimaryButton
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.SegmentMeter
import com.Tofwure2.Kestub.core.ui.StatCard
import com.Tofwure2.Kestub.core.ui.VaultBackground
import com.Tofwure2.Kestub.core.ui.VaultLockTile
import com.Tofwure2.Kestub.domain.model.Direction
import com.Tofwure2.Kestub.domain.model.PlateId
import com.Tofwure2.Kestub.domain.model.RoundSummary
import com.Tofwure2.Kestub.presentation.dialog.PauseDialog
import kotlin.math.floor

@Composable
fun GameScreen(
    stageIndex: Int,
    attempt: Int,
    onMenu: () -> Unit,
    onTutorial: () -> Unit,
    onFinished: (RoundSummary) -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(stageIndex, attempt) {
        viewModel.ensureStage(stageIndex, attempt)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.summary)
                is GameEvent.Pulse -> {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (event.strong) {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameContent(
        state = state,
        onTapCell = { index -> viewModel.tapCell(index) },
        onPry = { viewModel.togglePry() },
        onCharge = { viewModel.dropCharge() },
        onPause = { viewModel.openPause() },
        onResume = { viewModel.closePause() },
        onTutorial = {
            viewModel.closePause()
            onTutorial()
        },
        onMenu = {
            viewModel.closePause()
            onMenu()
        }
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onTapCell: (Int) -> Unit,
    onPry: () -> Unit,
    onCharge: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onTutorial: () -> Unit,
    onMenu: () -> Unit
) {
    val frameColor = plateColor(state.activePlate)
    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF0C0C10).copy(alpha = 0.88f),
                Color(0xFF13131A).copy(alpha = 0.80f),
                Color(0xFF0A0A0E).copy(alpha = 0.92f)
            )
        ),
        artAlpha = 0.70f,
        moteCount = 9,
        moteSeed = 31
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            GameHeader(
                stageIndex = state.stageIndex,
                stageNameRes = state.stageNameRes,
                coins = state.coins,
                onPause = onPause
            )
            Spacer(modifier = Modifier.height(8.dp))
            GameHud(state = state)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                VaultBoardView(
                    state = state,
                    frameColor = frameColor,
                    onTapCell = onTapCell
                )
                if (state.surgeShown) {
                    Text(
                        text = stringResource(R.string.surge_banner),
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 22.sp,
                        color = MintPrimary,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
            if (state.hintVisible) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.game_hint),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            GameControls(
                state = state,
                onPry = onPry,
                onCharge = onCharge
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
    if (state.paused) {
        PauseDialog(
            onResume = onResume,
            onTutorial = onTutorial,
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameHeader(
    stageIndex: Int,
    stageNameRes: Int,
    coins: Int,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlassPanel(
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onPause),
            shape = CircleShape,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = stringResource(
                R.string.game_stage,
                stageIndex,
                stringResource(stageNameRes)
            ),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 12.sp,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        CoinValue(value = coins, animated = true)
    }
}

@Composable
private fun GameHud(state: GameUiState) {
    val quotaState = stringResource(R.string.state_quota, state.coins, state.quota)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            label = stringResource(R.string.hud_quota),
            value = stringResource(R.string.hud_quota_value, state.coins, state.quota),
            valueColor = MintPrimary,
            modifier = Modifier
                .weight(1f)
                .semantics {
                    liveRegion = LiveRegionMode.Polite
                    stateDescription = quotaState
                },
            content = {
                MeterBar(
                    progress = state.quotaProgress,
                    fill = MintPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        )
        StatCard(
            label = stringResource(R.string.hud_strikes),
            value = stringResource(R.string.hud_strikes_value, state.strikesLeft),
            valueColor = if (state.strikesLeft <= 3) AlarmRed else GoldAccent,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = stringResource(R.string.hud_alarm),
            value = "",
            valueColor = AlarmRed,
            modifier = Modifier.weight(1f),
            content = {
                SegmentMeter(
                    filled = state.alarm,
                    total = 3,
                    fill = AlarmRed,
                    description = stringResource(R.string.state_alarm, state.alarm)
                )
            }
        )
    }
}

@Composable
private fun VaultBoardView(
    state: GameUiState,
    frameColor: Color,
    onTapCell: (Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val framePad: Dp = 8.dp
        val widthBudget = minOf(maxWidth - 8.dp, 380.dp)
        val tileByWidth = floorDp((widthBudget - framePad * 2) / state.cols)
        val tileByHeight = floorDp((maxHeight - framePad * 2) / state.rows)
        val tile = minOf(tileByWidth, tileByHeight).coerceAtLeast(24.dp)
        val boardWidth = tile * state.cols + framePad * 2
        val boardHeight = tile * state.rows + framePad * 2
        Box(
            modifier = Modifier
                .width(boardWidth)
                .height(boardHeight)
                .background(
                    SurfaceDeepPlate.copy(alpha = 0.92f),
                    RoundedCornerShape(22.dp)
                )
                .border(2.dp, frameColor.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
        ) {
            BoardFrameBrackets(color = GoldAccent)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(framePad)
            ) {
                for (row in 0 until state.rows) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (col in 0 until state.cols) {
                            val index = row * state.cols + col
                            val cell = state.cells.getOrNull(index)
                            VaultLockTile(
                                lockId = cell?.id ?: 0L,
                                armour = cell?.armour ?: 0,
                                direction = cell?.direction,
                                seal = cell?.seal == true,
                                tileSize = tile,
                                highContrast = state.highContrast,
                                enabled = !state.busy && !state.finished,
                                chainActive = state.chainActive.contains(index),
                                frameColor = frameColor,
                                description = tileDescription(cell),
                                stateLabel = tileState(cell),
                                onTap = { onTapCell(index) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameControls(
    state: GameUiState,
    onPry: () -> Unit,
    onCharge: () -> Unit
) {
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        shape = RoundedCornerShape(26.dp),
        borderColor = Color.White.copy(alpha = 0.10f),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_pry_tool),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
            }
            SecondaryButton(
                text = stringResource(R.string.pry_bar_count, state.pryCharges),
                enabled = state.pryCharges > 0 || state.pryArmed,
                accent = if (state.pryArmed) GoldAccent else TextPrimary,
                modifier = Modifier.weight(1f),
                onClick = onPry
            )
            PrimaryButton(
                text = stringResource(R.string.charge_action),
                height = 56,
                modifier = Modifier.weight(1.1f),
                iconRes = R.drawable.ic_bolt,
                onClick = onCharge
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = if (state.pryArmed) {
            stringResource(R.string.mode_pick_arrow)
        } else {
            stringResource(R.string.mode_pick_lock)
        },
        style = MaterialTheme.typography.labelSmall,
        fontSize = 10.sp,
        color = if (state.pryArmed) GoldAccent else MintPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun tileDescription(cell: LockCell?): String {
    if (cell == null || cell.armour <= 0 || cell.direction == null) {
        return stringResource(R.string.cd_empty_cell)
    }
    val dir = stringResource(directionLabel(cell.direction))
    return if (cell.seal) {
        stringResource(R.string.cd_seal_lock, cell.armour, dir)
    } else {
        stringResource(R.string.cd_lock, cell.armour, dir)
    }
}

@Composable
private fun tileState(cell: LockCell?): String =
    if (cell == null || cell.armour <= 0) {
        stringResource(R.string.cd_empty_cell)
    } else {
        stringResource(R.string.mission_progress, cell.armour, 3)
    }

private fun directionLabel(direction: Direction): Int = when (direction) {
    Direction.UP -> R.string.dir_up
    Direction.DOWN -> R.string.dir_down
    Direction.LEFT -> R.string.dir_left
    Direction.RIGHT -> R.string.dir_right
}

private fun plateColor(plate: PlateId?): Color = when (plate) {
    PlateId.COPPER -> PlateCopper
    PlateId.SILVER -> PlateSilver
    PlateId.GOLD -> PlateGold
    PlateId.PRIME -> PlatePrime
    null -> MintPrimary
}

private fun floorDp(value: Dp): Dp = Dp(floor(value.value))
