package com.example.runningapp.presentation.friend

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.runningapp.data.models.Friend

@Composable
fun FriendRow(friend: Friend, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .clickable(onClick = onClick, onClickLabel = "View friend details")
            .fillMaxSize()
            .padding(all = 12.dp)
    ) {
        AsyncImage(
            model = friend.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                .padding(all = 2.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = friend.nickname,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            )
            Text(text = "Online", style = MaterialTheme.typography.bodySmall)
        }
    }
}