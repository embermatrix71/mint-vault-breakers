package com.Tofwure2.Kestub.presentation.missions

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.Tofwure2.Kestub.core.ui.CoinValue
import com.Tofwure2.Kestub.core.ui.EmptyState
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.MeterBar
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.SectionHeader
import com.Tofwure2.Kestub.core.ui.VaultBackground

@Composable
fun MissionsScreen(onMenu: () -> Unit) {
    val viewModel: MissionsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF0E0E13).copy(alpha = 0.90f),
                Color(0xFF17161F).copy(alpha = 0.86f),
                Color(0xFF0A0A0E).copy(alpha = 0.94f)
            )
        ),
        artAlpha = 0.24f,
        moteCount = 10,
        moteSeed = 61
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SectionHeader(title = stringResource(R.string.missions_title), onHome = onMenu)
            Spacer(modifier = Modifier.height(4.dp))
            CoinValue(value = state.coinsTotal, animated = true)
            Spacer(modifier = Modifier.height(10.dp))
            if (state.allClaimed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        message = stringResource(R.string.missions_all_done),
                        artRes = R.drawable.sprite_4
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.rows) { row ->
                        MissionCard(row = row, onClaim = { viewModel.claim(row.mission.id) })
                    }
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
private fun MissionCard(row: MissionRow, onClaim: () -> Unit) {
    val claimedLabel = stringResource(R.string.state_claimed)
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .semantics {
                if (row.claimed) {
                    stateDescription = claimedLabel
                }
            },
        shape = RoundedCornerShape(18.dp),
        borderColor = if (row.complete && !row.claimed) {
            MintPrimary.copy(alpha = 0.45f)
        } else {
            Color.White.copy(alpha = 0.08f)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(row.mission.titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                MeterBar(
                    progress = row.progress.toFloat() / row.mission.target.toFloat(),
                    fill = if (row.claimed) GoldAccent else MintPrimary
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.mission_progress,
                            row.progress,
                            row.mission.target
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = stringResource(R.string.mission_reward, row.mission.reward),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = GoldAccent
                    )
                }
            }
            if (row.claimed) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = MintPrimary,
                        modifier = Modifier.size(16.dp)
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
                    enabled = row.complete,
                    accent = MintPrimary,
                    onClick = onClaim
                )
            }
        }
    }
}
