package com.Tofwure2.Kestub.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Tofwure2.Kestub.core.theme.SurfaceAlt

@Composable
fun MeterBar(
    progress: Float,
    fill: Color,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    track: Color = SurfaceAlt
) {
    val clamped = progress.coerceIn(0f, 1f)
    val animated by animateFloatAsState(
        targetValue = clamped,
        animationSpec = tween(durationMillis = 420),
        label = "meter"
    )
    val shape = RoundedCornerShape(height / 2)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(track, shape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animated)
                .fillMaxHeight()
                .background(Brush.horizontalGradient(listOf(fill.copy(alpha = 0.7f), fill)), shape)
        )
    }
}
