package com.example.runningapp.presentation.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.runningapp.R

@Composable
fun GetStarted(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.imagegetstarted),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.onBackground)
            .fillMaxSize()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Running App",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
        )
        Text(
            text = stringResource(id = R.string.get_started_title),
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 20.dp)
        )
        Button(
            onClick = { navController.navigate("onBoarding") },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 2.dp
            ),
        ) {
            Text(
                text = "BẮT ĐẦU",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GetStarted()
}*/