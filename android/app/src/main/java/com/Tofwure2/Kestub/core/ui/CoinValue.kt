package com.Tofwure2.Kestub.core.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.theme.GoldAccent

@Composable
fun CoinValue(
    value: Int,
    modifier: Modifier = Modifier,
    color: Color = GoldAccent,
    fontSize: TextUnit = 16.sp,
    iconSize: Int = 16,
    animated: Boolean = false
) {
    val shown by animateIntAsState(
        targetValue = value,
        animationSpec = tween(durationMillis = if (animated) 360 else 0),
        label = "coins"
    )
    Row(
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_coin),
            contentDescription = stringResource(R.string.cd_coin),
            tint = color,
            modifier = Modifier.size(iconSize.dp)
        )
        Text(
            text = shown.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontSize = fontSize,
            color = color
        )
    }
}
