package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simovic.simplegaming.base.presentation.ui.AppTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label, style = AppTheme.typo.body3) },
        textStyle = AppTheme.typo.body1,
        isError = isError,
        shape = RoundedCornerShape(12.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.color.brandPrimary,
                unfocusedBorderColor = AppTheme.color.divider,
                focusedLabelColor = AppTheme.color.brandPrimary,
                unfocusedLabelColor = AppTheme.color.textMuted,
                errorBorderColor = AppTheme.color.error,
                cursorColor = AppTheme.color.brandPrimary,
            ),
    )
}
