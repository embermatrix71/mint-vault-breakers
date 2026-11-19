package com.Tofwure2.Kestub.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Tofwure2.Kestub.core.theme.TextSecondary

@Composable
fun EmptyState(
    message: String,
    artRes: Int,
    modifier: Modifier = Modifier,
    artSize: Int = 72
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Image(
            painter = painterResource(artRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(artSize.dp)
                .alpha(0.45f)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
