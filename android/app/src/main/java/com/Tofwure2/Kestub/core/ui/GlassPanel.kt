package com.Tofwure2.Kestub.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Tofwure2.Kestub.core.theme.SurfaceRaised

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    container: Color = SurfaceRaised.copy(alpha = 0.82f),
    borderColor: Color = Color.White.copy(alpha = 0.08f),
    elevation: Dp = 2.dp,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation, shape)
            .background(container, shape)
            .border(1.dp, borderColor, shape),
        contentAlignment = contentAlignment,
        content = content
    )
}
