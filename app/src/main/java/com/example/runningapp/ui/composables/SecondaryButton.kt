package com.example.runningapp.ui.composables

import androidx.annotation.ColorInt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils


inline val @receiver:ColorInt Color.darken
    @ColorInt
    get() = Color(ColorUtils.blendARGB(this.toArgb(), Color.Black.toArgb(), 0.2f))

@Composable
fun SecondaryButton(
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit),
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.secondaryContainer),
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.darken,
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