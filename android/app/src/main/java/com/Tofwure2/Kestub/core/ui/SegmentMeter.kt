package com.Tofwure2.Kestub.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Tofwure2.Kestub.core.theme.SurfaceAlt

@Composable
fun SegmentMeter(
    filled: Int,
    total: Int,
    fill: Color,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.semantics { stateDescription = description },
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        for (index in 0 until total) {
            Box(
                modifier = Modifier
                    .width(14.dp)
                    .height(7.dp)
                    .background(
                        color = if (index < filled) fill else SurfaceAlt,
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}
