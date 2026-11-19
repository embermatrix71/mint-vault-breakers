package com.Tofwure2.Kestub.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.theme.ArmourHeavy
import com.Tofwure2.Kestub.core.theme.ArmourLight
import com.Tofwure2.Kestub.core.theme.ArmourMedium
import com.Tofwure2.Kestub.core.theme.GoldAccent
import com.Tofwure2.Kestub.core.theme.MintInk
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.SurfaceAlt
import com.Tofwure2.Kestub.core.theme.TextPrimary
import com.Tofwure2.Kestub.domain.model.Direction

@Composable
fun VaultLockTile(
    lockId: Long,
    armour: Int,
    direction: Direction?,
    seal: Boolean,
    tileSize: Dp,
    highContrast: Boolean,
    enabled: Boolean,
    chainActive: Boolean,
    frameColor: Color,
    description: String,
    stateLabel: String,
    onTap: () -> Unit
) {
    val pop = remember { Animatable(1f) }
    LaunchedEffect(lockId) {
        if (lockId > 0L) {
            pop.snapTo(0.72f)
            pop.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow))
        }
    }
    val glow by animateFloatAsState(
        targetValue = if (chainActive) 1f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "chainGlow"
    )
    Box(
        modifier = Modifier
            .size(tileSize)
            .padding(3.dp)
            .semantics { stateDescription = stateLabel },
        contentAlignment = Alignment.Center
    ) {
        if (armour <= 0 || direction == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.26f), RoundedCornerShape(14.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(14.dp))
            )
            return@Box
        }
        val plateTint = when (armour) {
            1 -> ArmourLight
            2 -> ArmourMedium
            else -> ArmourHeavy
        }
        val rim = when {
            seal -> GoldAccent
            armour == 1 -> MintPrimary.copy(alpha = 0.55f)
            else -> Color.White.copy(alpha = 0.08f)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = pop.value
                    scaleY = pop.value
                }
                .background(
                    Brush.verticalGradient(
                        listOf(
                            plateTint.copy(alpha = 0.92f),
                            plateTint.copy(alpha = 0.58f)
                        )
                    ),
                    RoundedCornerShape(14.dp)
                )
                .border(if (seal) 2.dp else 1.dp, rim, RoundedCornerShape(14.dp))
                .clickable(enabled = enabled, onClick = onTap),
            contentAlignment = Alignment.Center
        ) {
            if (glow > 0.01f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MintPrimary.copy(alpha = 0.35f * glow),
                            RoundedCornerShape(14.dp)
                        )
                )
            }
            Image(
                painter = painterResource(if (seal) R.drawable.sprite_2 else R.drawable.sprite_1),
                contentDescription = description,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(tileSize - 24.dp)
                    .graphicsLayer { alpha = 0.95f }
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                for (pip in 0 until armour) {
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(3.dp)
                            .background(
                                TextPrimary.copy(alpha = if (highContrast) 1f else 0.8f),
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(20.dp)
                    .background(
                        if (highContrast) MintInk else Color.Transparent,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_dir),
                    contentDescription = null,
                    tint = if (highContrast) Color.White else MintPrimary,
                    modifier = Modifier
                        .size(15.dp)
                        .graphicsLayer { rotationZ = rotationFor(direction) }
                )
            }
            if (seal) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(7.dp)
                        .background(GoldAccent, CircleShape)
                )
            }
        }
        if (!enabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceAlt.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
            )
        }
    }
}

private fun rotationFor(direction: Direction): Float = when (direction) {
    Direction.LEFT -> 0f
    Direction.UP -> 90f
    Direction.RIGHT -> 180f
    Direction.DOWN -> 270f
}

@Composable
fun BoardFrameBrackets(color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        listOf(
            Alignment.TopStart,
            Alignment.TopEnd,
            Alignment.BottomStart,
            Alignment.BottomEnd
        ).forEach { corner ->
            Box(
                modifier = Modifier
                    .align(corner)
                    .padding(6.dp)
                    .size(14.dp)
                    .border(2.dp, color.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            )
        }
    }
}
