package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simovic.simplegaming.base.presentation.ui.AppTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp), // Designer standard height
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(8.dp), // Designer's corner radius
        colors =
            ButtonDefaults.buttonColors(
                containerColor = AppTheme.color.brandPrimary,
                contentColor = Color.White,
                disabledContainerColor = AppTheme.color.divider,
                disabledContentColor = AppTheme.color.textMuted,
            ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Text(text = text, style = AppTheme.typo.head5)
        }
    }
}
