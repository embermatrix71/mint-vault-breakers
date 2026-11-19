package com.Tofwure2.Kestub.presentation.levelmap

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.SectionHeader
import com.Tofwure2.Kestub.core.ui.VaultBackground
import com.Tofwure2.Kestub.data.sample.SampleData
import com.Tofwure2.Kestub.domain.model.StageSpec

@Composable
fun LevelMapScreen(
    onMenu: () -> Unit,
    onPlay: (Int) -> Unit
) {
    val viewModel: LevelMapViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF0E0E13).copy(alpha = 0.90f),
                Color(0xFF101A18).copy(alpha = 0.86f),
                Color(0xFF0A0A0E).copy(alpha = 0.94f)
            )
        ),
        artAlpha = 0.28f,
        moteCount = 11,
        moteSeed = 51
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SectionHeader(title = stringResource(R.string.level_map_title), onHome = onMenu)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(SampleData.stages) { stage ->
                    StageCard(
                        stage = stage,
                        unlocked = stage.index <= state.stagesUnlocked,
                        best = state.stageBest[stage.index] ?: 0,
                        sealOwned = state.seals.contains(stage.sealId),
                        onPlay = { onPlay(stage.index) }
                    )
                }
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
private fun StageCard(
    stage: StageSpec,
    unlocked: Boolean,
    best: Int,
    sealOwned: Boolean,
    onPlay: () -> Unit
) {
    val lockedLabel = stringResource(R.string.state_locked)
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .padding(start = if (stage.index % 2 == 0) 18.dp else 0.dp)
            .semantics {
                if (!unlocked) {
                    stateDescription = lockedLabel
                }
            },
        shape = RoundedCornerShape(18.dp),
        borderColor = if (unlocked) MintPrimary.copy(alpha = 0.26f) else Color.White.copy(alpha = 0.06f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .alpha(if (unlocked) 1f else 0.45f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(if (sealOwned) 40.dp else 32.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(stage.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    color = if (unlocked) TextPrimary else TextTertiary
                )
                Spacer(modifier = Modifier.height(3.dp))
                if (unlocked) {
                    Text(
                        text = stringResource(R.string.stage_quota, stage.quota, stage.strikes),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                    if (best > 0) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.stage_best, best),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = GoldAccent
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.stage_locked, stage.index - 1),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = TextTertiary
                    )
                }
            }
            if (unlocked) {
                SecondaryButton(
                    text = stringResource(R.string.play),
                    accent = MintPrimary,
                    onClick = onPlay
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = stringResource(R.string.locked),
                    tint = TextTertiary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
