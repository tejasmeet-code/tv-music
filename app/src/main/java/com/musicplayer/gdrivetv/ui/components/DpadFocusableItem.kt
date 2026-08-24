package com.musicplayer.gdrivetv.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme

@Composable
fun DpadFocusableItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    normalBackgroundColor: Color = Color.Transparent,
    focusedBackgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
    content: @Composable BoxScope.(isFocused: Boolean) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val animatedBgColor by animateColorAsState(
        targetValue = if (isFocused) focusedBackgroundColor else normalBackgroundColor,
        label = "bgColor"
    )

    Box(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused }
            .clip(RoundedCornerShape(8.dp))
            .background(animatedBgColor)
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) focusedBorderColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .focusable(),
        contentAlignment = androidx.compose.ui.Alignment.CenterStart
    ) {
        content(isFocused)
    }
}
