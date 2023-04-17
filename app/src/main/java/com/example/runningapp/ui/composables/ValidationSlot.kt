package com.example.runningapp.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.runningapp.domain.models.Validation

@Composable
fun ValidationSlot(
    validation: Validation,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment, modifier = modifier) {
        content()
        AnimatedVisibility(
            visible = validation is Validation.Error,
            modifier = Modifier.align(Alignment.Start).padding(top = 3.dp, start = 12.dp, end = 12.dp)
        ) {
            Text(
                text = (validation as? Validation.Error)?.message ?: "",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}