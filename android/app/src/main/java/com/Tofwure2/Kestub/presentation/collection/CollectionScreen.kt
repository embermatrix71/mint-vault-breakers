package com.Tofwure2.Kestub.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.ColorFilter
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
import com.Tofwure2.Kestub.core.theme.SurfaceAlt
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.theme.TextTertiary
import com.Tofwure2.Kestub.core.ui.EmptyState
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.SecondaryButton
import com.Tofwure2.Kestub.core.ui.SectionHeader
import com.Tofwure2.Kestub.core.ui.VaultBackground

@Composable
fun CollectionScreen(onMenu: () -> Unit) {
    val viewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF0B1113).copy(alpha = 0.90f),
                Color(0xFF131B1E).copy(alpha = 0.86f),
                Color(0xFF0A0A0E).copy(alpha = 0.94f)
            )
        ),
        artAlpha = 0.22f,
        moteCount = 9,
        moteSeed = 81
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SectionHeader(title = stringResource(R.string.collection_title), onHome = onMenu)
            Text(
                text = stringResource(R.string.collection_hint),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (state.ownedCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        message = stringResource(R.string.collection_empty),
                        artRes = R.drawable.sprite_2
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.rows.chunked(3).forEach { triple ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            triple.forEach { row ->
                                CollectibleTile(
                                    row = row,
                                    modifier = Modifier.weight(1f),
                                    onSelect = {
                                        val plate = row.item.plate
                                        if (plate != null) {
                                            viewModel.selectPlate(plate)
                                        }
                                    }
                                )
                            }
                            if (triple.size < 3) {
                                Spacer(modifier = Modifier.weight((3 - triple.size).toFloat()))
                            }
                        }
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
private fun CollectibleTile(
    row: CollectibleRow,
    modifier: Modifier,
    onSelect: () -> Unit
) {
    val lockedLabel = stringResource(R.string.state_locked)
    val ownedLabel = stringResource(R.string.state_owned)
    val activeLabel = stringResource(R.string.state_active)
    val selectable = row.owned && row.item.plate != null
    GlassPanel(
        modifier = modifier
            .height(104.dp)
            .semantics {
                stateDescription = when {
                    !row.owned -> lockedLabel
                    row.active -> activeLabel
                    else -> ownedLabel
                }
            }
            .clickable(enabled = selectable, onClick = onSelect),
        shape = RoundedCornerShape(16.dp),
        borderColor = when {
            row.active -> GoldAccent.copy(alpha = 0.6f)
            row.owned -> MintPrimary.copy(alpha = 0.34f)
            else -> Color.White.copy(alpha = 0.06f)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(
                        if (row.item.seal) R.drawable.sprite_2 else R.drawable.sprite_4
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = if (row.owned) null else ColorFilter.tint(SurfaceAlt),
                    modifier = Modifier.size(42.dp)
                )
                if (row.active) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (row.owned) {
                    stringResource(row.item.titleRes)
                } else {
                    stringResource(R.string.locked)
                },
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = if (row.owned) TextPrimary else TextTertiary,
                textAlign = TextAlign.Center
            )
            if (row.active) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(R.string.active),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = GoldAccent
                )
            }
        }
    }
}
