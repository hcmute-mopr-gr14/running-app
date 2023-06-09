package com.example.runningapp.presentation.intro

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.runningapp.R
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
@Composable
fun OnBoarding(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row() {
            IconButton(
                onClick = { navController.navigate("getStarted") },
            ) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier
                        .size(width = 40.dp, height = 30.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                )
            }
            Spacer(modifier = Modifier.width(240.dp))
            TextButton(
                onClick = { navController.navigate("login") },
            ) {
                Text(
                    text = stringResource(R.string.skip_button),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.onboarding_image),
            contentDescription = "",
            modifier = Modifier
                .size(width = 400.dp, height = 300.dp)
                .padding(top = 50.dp)
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val items = ArrayList<OnBoardingData>()
            items.add(
                OnBoardingData(
                    title = stringResource(id = R.string.on_boarding_title),
                    describe = stringResource(id = R.string.des_1)
                )
            )
            items.add(
                OnBoardingData(
                    title = stringResource(id = R.string.on_boarding_title),
                    describe = stringResource(id = R.string.des_2)
                )
            )
            items.add(
                OnBoardingData(
                    title = stringResource(id = R.string.on_boarding_title),
                    describe = stringResource(id = R.string.des_3)
                )
            )
            val pageState = rememberPagerState(
                initialPage = 0
            )
            OnBoardingPager(
                item = items,
                pagerState = pageState,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 340.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.on_boarding_question),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(
                        onClick = { navController.navigate("signUp") },
                        contentPadding = PaddingValues(0.dp)
                    )
                    {
                        Text(
                            text = stringResource(id = R.string.on_boarding_text_button),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@DelicateCoroutinesApi
@Composable
fun OnBoardingPager(
    item: List<OnBoardingData>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(state = pagerState, pageCount = 3) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(width = 311.dp, height = 303.dp)
                            .padding(top = 40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(corner = CornerSize(64.dp))
                            )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = item[page].title,
                                color = MaterialTheme.colorScheme.background,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                            Text(
                                text = item[page].describe,
                                color = MaterialTheme.colorScheme.background,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(vertical = 45.dp, horizontal = 20.dp)
                            )
                            PagerIndicator(items = item, currentPage = pagerState.currentPage)
                            /*Button(
                                onClick = { },
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .size(width = 150.dp, height = 56.dp),
                                shape = RoundedCornerShape(corner = CornerSize(12.dp))
                            ) {
                                Text(
                                    text = "Next",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = ""
                                )
                            }*/
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PagerIndicator(currentPage: Int, items: List<OnBoardingData>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(items.size) {
            Indicator(
                isSelected = it == currentPage,
                color = Color(red = 241, green = 73, blue = 133)
            )
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean, color: Color) {
    val width = animateDpAsState(targetValue = if (isSelected) 40.dp else 10.dp)
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) color else Color.Gray.copy(alpha = 0.5f)
            )
    )
}