package com.simovic.simplegaming.base.presentation.compose.composable

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthDayWheelPicker(
    onChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    date: LocalDate = LocalDate.of(2000, 1, 1),
) {
    val currentOnChange by rememberUpdatedState(onChange)
    val currentDay = date.dayOfMonth
    val currentMonth = date.monthValue
    val currentYear = date.year

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        WheelPicker(
            values = (1..12).toList(),
            selected = currentMonth,
            modifier = Modifier.width(100.dp),
            onChange = { newMonth ->
                val maxDay = YearMonth.of(currentYear, newMonth).lengthOfMonth()
                val safeDay = currentDay.coerceAtMost(maxDay)
                currentOnChange(LocalDate.of(currentYear, newMonth, safeDay))
            },
        )

        WheelPicker(
            values = (1..31).toList(),
            selected = currentDay,
            modifier = Modifier.width(100.dp),
            onChange = { newDay ->
                val maxDay = date.lengthOfMonth()
                val safeDay = newDay.coerceAtMost(maxDay)
                currentOnChange(LocalDate.of(currentYear, currentMonth, safeDay))
            },
        )
    }
}

@Composable
fun WheelPicker(
    values: List<Int>,
    selected: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnChange by rememberUpdatedState(onChange)

    val itemHeight = 40.dp
    val listState =
        rememberLazyListState(
            initialFirstVisibleItemIndex = values.indexOf(selected),
        )

    LaunchedEffect(listState.firstVisibleItemIndex) {
        val index = listState.firstVisibleItemIndex
        if (index in values.indices) {
            currentOnChange(values[index])
        }
    }

    Box(
        modifier = modifier.height(itemHeight * 3),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = rememberSnapFlingBehavior(listState),
        ) {
            items(values.size) { index ->
                val value = values[index]
                Box(
                    modifier = Modifier.height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = value.toString(),
                        fontSize = if (value == selected) 20.sp else 16.sp,
                        fontWeight = if (value == selected) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}
