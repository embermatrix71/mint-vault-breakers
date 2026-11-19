package com.Tofwure2.Kestub.presentation.menu

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
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
import com.Tofwure2.Kestub.core.navigation.Screen
import com.Tofwure2.Kestub.core.theme.GoldAccent
import com.Tofwure2.Kestub.core.theme.MintDark
import com.Tofwure2.Kestub.core.theme.MintInk
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.SurfaceRaised
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.theme.TextTertiary
import com.Tofwure2.Kestub.core.ui.CoinValue
import com.Tofwure2.Kestub.core.ui.VaultBackground

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onOpen: (Screen) -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    MenuContent(state = state, onPlay = onPlay, onOpen = onOpen)
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: (Int) -> Unit,
    onOpen: (Screen) -> Unit
) {
    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF111114).copy(alpha = 0.80f),
                Color(0xFF16161C).copy(alpha = 0.74f),
                Color(0xFF0E0E13).copy(alpha = 0.88f)
            )
        ),
        artAlpha = 0.55f,
        moteCount = 12,
        moteSeed = 11
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(MintPrimary.copy(alpha = 0.16f), Color.Transparent),
                        radius = 620f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            MenuHeader(coins = state.coins)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.menu_hero),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 21.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.menu_stage_line,
                    state.currentStage,
                    stringResource(state.currentStageNameRes)
                ),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                color = MintPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayTile(
                    stage = state.currentStage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.45f),
                    onClick = { onPlay(state.currentStage) }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.15f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BentoTile(
                        label = stringResource(R.string.levels),
                        caption = stringResource(R.string.levels_caption, state.stagesUnlocked),
                        spriteRes = R.drawable.sprite_2,
                        accent = GoldAccent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onOpen(Screen.LevelMap) }
                    )
                    BentoTile(
                        label = stringResource(R.string.missions),
                        caption = stringResource(R.string.missions_caption, state.missionsDone),
                        iconRes = R.drawable.ic_target,
                        accent = MintPrimary,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onOpen(Screen.Missions) }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.15f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BentoTile(
                        label = stringResource(R.string.collection),
                        caption = stringResource(
                            R.string.collection_caption,
                            state.collectionOwned
                        ),
                        spriteRes = R.drawable.sprite_5,
                        accent = MintPrimary,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onOpen(Screen.Collection) }
                    )
                    BentoTile(
                        label = stringResource(R.string.rewards),
                        caption = if (state.cratesReady > 0) {
                            stringResource(R.string.rewards_caption, state.cratesReady)
                        } else {
                            stringResource(R.string.rewards_caption_idle)
                        },
                        spriteRes = R.drawable.sprite_4,
                        accent = GoldAccent,
                        highlighted = state.cratesReady > 0,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onOpen(Screen.Rewards) }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.85f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BentoTile(
                        label = stringResource(R.string.tutorial),
                        iconRes = R.drawable.ic_help,
                        accent = MintPrimary,
                        highlighted = !state.tutorialSeen,
                        compact = true,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onOpen(Screen.Tutorial) }
                    )
                    BentoTile(
                        label = stringResource(R.string.settings),
                        iconRes = R.drawable.ic_settings,
                        accent = TextSecondary,
                        compact = true,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { onOpen(Screen.Settings) }
                    )
                }
            }
            if (state.bestVault > 0 && state.longestChain > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(
                        R.string.menu_footer,
                        state.bestVault,
                        state.longestChain
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextTertiary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun MenuHeader(coins: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.brand_short),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .height(40.dp)
                .background(SurfaceRaised.copy(alpha = 0.86f), RoundedCornerShape(20.dp))
                .border(1.dp, GoldAccent.copy(alpha = 0.26f), RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            CoinValue(value = coins, animated = true)
        }
    }
}

@Composable
private fun PlayTile(stage: Int, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.975f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
        label = "playScale"
    )
    Button(
        onClick = onClick,
        shape = shape,
        interactionSource = interaction,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(20.dp, shape, ambientColor = MintPrimary, spotColor = MintPrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(MintPrimary, MintDark)), shape)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 18.dp)
                    .size(72.dp)
                    .alpha(0.32f)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 22.dp)
            ) {
                Text(
                    text = stringResource(R.string.play),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 30.sp,
                    color = MintInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.menu_play_caption, stage),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MintInk.copy(alpha = 0.72f)
                )
            }
        }
    }
}

@Composable
private fun BentoTile(
    label: String,
    accent: Color,
    modifier: Modifier,
    caption: String? = null,
    spriteRes: Int? = null,
    iconRes: Int? = null,
    highlighted: Boolean = false,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Button(
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceRaised.copy(alpha = 0.84f), shape)
                .border(
                    1.dp,
                    if (highlighted) accent.copy(alpha = 0.55f) else Color.White.copy(alpha = 0.08f),
                    shape
                )
                .padding(14.dp)
        ) {
            if (spriteRes != null) {
                Image(
                    painter = painterResource(spriteRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(if (compact) 24.dp else 34.dp)
                        .alpha(0.9f)
                )
            }
            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(if (compact) 20.dp else 26.dp)
                )
            }
            if (highlighted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(8.dp)
                        .background(accent, CircleShape)
                )
            }
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                if (caption != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = caption,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
