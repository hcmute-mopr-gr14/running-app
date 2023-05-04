package com.example.runningapp.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit),
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primaryContainer),
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.secondaryContainer)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 14.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        ),
        content = content
    )
}