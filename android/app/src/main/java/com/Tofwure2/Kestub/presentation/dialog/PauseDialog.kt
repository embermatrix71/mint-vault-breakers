package com.Tofwure2.Kestub.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.Tofwure2.Kestub.R
import com.Tofwure2.Kestub.core.theme.MintPrimary
import com.Tofwure2.Kestub.core.theme.TextSecondary
import com.Tofwure2.Kestub.core.ui.GlassPanel
import com.Tofwure2.Kestub.core.ui.PrimaryButton
import com.Tofwure2.Kestub.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onTutorial: () -> Unit,
    onMenu: () -> Unit
) {
    Dialog(
        onDismissRequest = onResume,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        GlassPanel(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            borderColor = MintPrimary.copy(alpha = 0.3f),
            elevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.pause_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp,
                    color = MintPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.pause_body),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                PrimaryButton(
                    text = stringResource(R.string.resume),
                    modifier = Modifier.fillMaxWidth(),
                    height = 56,
                    onClick = onResume
                )
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SecondaryButton(
                        text = stringResource(R.string.tutorial),
                        modifier = Modifier.weight(1f),
                        onClick = onTutorial
                    )
                    SecondaryButton(
                        text = stringResource(R.string.menu),
                        modifier = Modifier.weight(1f),
                        onClick = onMenu
                    )
                }
            }
        }
    }
}
