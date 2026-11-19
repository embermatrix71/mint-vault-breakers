package com.Tofwure2.Kestub.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Tofwure2.Kestub.core.theme.MintDark
import com.Tofwure2.Kestub.core.theme.MintInk
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.SurfaceAlt
import com.Tofwure2.Kestub.core.theme.TextTertiary

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Int = 60,
    iconRes: Int? = null,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
        label = "primaryScale"
    )
    val brush = if (enabled) {
        Brush.linearGradient(listOf(MintPrimary, MintDark))
    } else {
        Brush.linearGradient(listOf(SurfaceAlt, SurfaceAlt))
    }
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        interactionSource = interaction,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MintInk,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextTertiary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        modifier = modifier
            .height(height.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 16.dp else 0.dp,
                shape = shape,
                ambientColor = MintPrimary,
                spotColor = MintPrimary
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush, shape),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (iconRes != null) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = if (enabled) MintInk else TextTertiary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    color = if (enabled) MintInk else TextTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
