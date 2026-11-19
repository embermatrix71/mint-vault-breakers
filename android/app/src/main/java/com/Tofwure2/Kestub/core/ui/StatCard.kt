package com.Tofwure2.Kestub.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Tofwure2.Kestub.core.theme.TextSecondary

@Composable
fun StatCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    content: @Composable (() -> Unit)? = null
) {
    if (value.isBlank() && content == null) return
    GlassPanel(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        borderColor = valueColor.copy(alpha = 0.33f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (iconRes != null) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = valueColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(valueColor, CircleShape)
                )
            }
            if (content != null) {
                content()
            }
            if (value.isNotBlank()) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 19.sp,
                    color = valueColor,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
