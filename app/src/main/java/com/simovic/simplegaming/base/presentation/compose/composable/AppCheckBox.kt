package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.simovic.simplegaming.base.presentation.ui.AppTheme

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors =
            CheckboxDefaults.colors(
                checkedColor = AppTheme.color.brandPrimary,
                uncheckedColor = AppTheme.color.textMuted,
                checkmarkColor = AppTheme.color.surfaceHigh,
            ),
    )
}
