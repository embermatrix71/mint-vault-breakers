package com.Tofwure2.Kestub.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.Tofwure2.Kestub.core.ui.VaultBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        revealed = true
    }
    val emblemScale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.82f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow),
        label = "emblemScale"
    )
    val emblemAlpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 520),
        label = "emblemAlpha"
    )

    VaultBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF05050A).copy(alpha = 0.94f),
                Color(0xFF0D0D12).copy(alpha = 0.92f),
                Color(0xFF05050A).copy(alpha = 0.97f)
            )
        ),
        artAlpha = 0.35f,
        moteCount = 18,
        moteSeed = 3
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(228.dp),
                contentAlignment = Alignment.Center
            ) {
                VaultDial()
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer {
                            scaleX = emblemScale
                            scaleY = emblemScale
                            alpha = emblemAlpha
                        }
                )
            }
            Spacer(modifier = Modifier.height(34.dp))
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.brand_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 28.sp,
                    color = MintPrimary.copy(alpha = 0.22f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { translationY = 5f }
                )
                Text(
                    text = stringResource(R.string.brand_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 28.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { alpha = emblemAlpha }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                letterSpacing = 4.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(46.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .width(168.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = MintPrimary,
                trackColor = SurfaceAlt
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                letterSpacing = 5.sp,
                color = TextTertiary
            )
        }
    }
}

@Composable
private fun VaultDial() {
    val transition = androidx.compose.animation.core.rememberInfiniteTransition(label = "dial")
    val outerTurn by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outerTurn"
    )
    val innerTurn by transition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "innerTurn"
    )
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(
            color = MintPrimary.copy(alpha = 0.14f),
            radius = size.minDimension * 0.52f,
            center = center
        )
        drawCircle(
            color = Color(0xFF2A2A33),
            radius = size.minDimension * 0.48f,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
        rotate(outerTurn, center) {
            drawArc(
                color = MintPrimary.copy(alpha = 0.45f),
                startAngle = 0f,
                sweepAngle = 120f,
                useCenter = false,
                topLeft = Offset(
                    center.x - size.minDimension * 0.40f,
                    center.y - size.minDimension * 0.40f
                ),
                size = androidx.compose.ui.geometry.Size(
                    size.minDimension * 0.80f,
                    size.minDimension * 0.80f
                ),
                style = Stroke(width = 3.dp.toPx())
            )
        }
        rotate(innerTurn, center) {
            drawArc(
                color = GoldAccent.copy(alpha = 0.30f),
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(
                    center.x - size.minDimension * 0.34f,
                    center.y - size.minDimension * 0.34f
                ),
                size = androidx.compose.ui.geometry.Size(
                    size.minDimension * 0.68f,
                    size.minDimension * 0.68f
                ),
                style = Stroke(width = 2.dp.toPx())
            )
        }
        drawCircle(
            color = GoldAccent.copy(alpha = 0.22f),
            radius = size.minDimension * 0.30f * pulse,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}
