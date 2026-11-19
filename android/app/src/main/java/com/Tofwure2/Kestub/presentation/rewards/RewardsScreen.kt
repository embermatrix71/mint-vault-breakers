package com.Tofwure2.Kestub.presentation.rewards

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.di.AppViewModelFactory
import com.Tofwure2.Kestub.core.theme.GoldAccent
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.theme.TextTertiary
import com.Tofwure2.Kestub.core.ui.CoinValue
import com.Tofwure2.Kestub.core.ui.EmptyState
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.SectionHeader
import com.Tofwure2.Kestub.core.ui.VaultBackground

@Composable
fun RewardsScreen(onMenu: () -> Unit) {
    val viewModel: RewardsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF12100B).copy(alpha = 0.90f),
                Color(0xFF1A1611).copy(alpha = 0.86f),
                Color(0xFF0A0A0E).copy(alpha = 0.94f)
            )
        ),
        artAlpha = 0.26f,
        moteCount = 13,
        moteSeed = 71
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SectionHeader(title = stringResource(R.string.rewards_title), onHome = onMenu)
            Text(
                text = stringResource(R.string.rewards_disclaimer),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))
            CoinValue(value = state.coinsTotal, animated = true)
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.rows.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { row ->
                            CrateTile(
                                row = row,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                onClaim = { viewModel.claim(row.crate.id) }
                            )
                        }
                    }
                }
            }
            if (!state.anyReady) {
                Spacer(modifier = Modifier.height(10.dp))
                EmptyState(
                    message = stringResource(R.string.rewards_empty),
                    artRes = R.drawable.sprite_4,
                    artSize = 40
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                modifier = Modifier.fillMaxWidth(),
                onClick = onMenu
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun CrateTile(
    row: CrateRow,
    modifier: Modifier,
    onClaim: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "crate")
    val glow by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crateGlow"
    )
    val readyLabel = stringResource(R.string.state_ready)
    val claimedLabel = stringResource(R.string.state_claimed)
    GlassPanel(
        modifier = modifier.semantics {
            stateDescription = if (row.claimed) claimedLabel else readyLabel
        },
        shape = RoundedCornerShape(20.dp),
        borderColor = when {
            row.ready -> GoldAccent.copy(alpha = glow)
            row.claimed -> MintPrimary.copy(alpha = 0.32f)
            else -> Color.White.copy(alpha = 0.07f)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_crate),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(54.dp)
                    .alpha(if (row.claimed || row.ready) 1f else 0.42f)
            )
            Text(
                text = stringResource(row.crate.titleRes),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 12.sp,
                color = if (row.ready || row.claimed) TextPrimary else TextTertiary,
                textAlign = TextAlign.Center
            )
            Text(
                text = when {
                    row.claimed -> stringResource(row.crate.plateTitleRes)
                    row.ready -> stringResource(R.string.crate_ready)
                    else -> stringResource(R.string.crate_needs, row.missing)
                },
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = if (row.ready) GoldAccent else TextSecondary,
                textAlign = TextAlign.Center
            )
            if (row.claimed) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = MintPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = stringResource(R.string.claimed),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = MintPrimary
                    )
                }
            } else {
                SecondaryButton(
                    text = stringResource(R.string.claim),
                    enabled = row.ready,
                    accent = GoldAccent,
                    onClick = onClaim
                )
            }
        }
    }
}
