package com.Tofwure2.Kestub.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.theme.BackgroundVoid
import com.Tofwure2.Kestub.core.theme.GoldAccent
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun VaultBackground(
    overlay: Brush,
    artAlpha: Float,
    moteCount: Int = 14,
    moteSeed: Int = 7,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundVoid)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(artAlpha),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlay)
        )
        CoinMotes(count = moteCount, seed = moteSeed)
        content()
    }
}

@Composable
private fun CoinMotes(count: Int, seed: Int) {
    val motes = remember(count, seed) {
        val random = Random(seed * 31L + count)
        List(count) {
            MoteSpec(
                x = random.nextFloat(),
                phase = random.nextFloat(),
                radius = 1.2f + random.nextFloat() * 1.8f,
                alpha = 0.08f + random.nextFloat() * 0.14f,
                drift = (random.nextFloat() - 0.5f) * 0.12f
            )
        }
    }
    val transition = rememberInfiniteTransition(label = "motes")
    val travel by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "travel"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        motes.forEach { mote ->
            val progress = (travel + mote.phase) % 1f
            val y = size.height * (1f - progress)
            val x = size.width * ((mote.x + mote.drift * progress).absoluteValue % 1f)
            drawCircle(
                color = GoldAccent,
                radius = mote.radius * density,
                center = Offset(x, y),
                alpha = mote.alpha * (1f - (progress - 0.5f).absoluteValue)
            )
        }
    }
}

private data class MoteSpec(
    val x: Float,
    val phase: Float,
    val radius: Float,
    val alpha: Float,
    val drift: Float
)
