package com.longing.awesomeview.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RainbowLoader(
    modifier: Modifier = Modifier,
    arcSpacing: Dp = 3.dp,
    initialRadius: Dp = 5.dp,
    strokeWidth: Dp = 4.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
    arcColors: List<Color> = listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue)
) {
    // 基于圆弧数量计算动画时常
    val duration = remember(arcColors) { 1000 + (arcColors.size) * 100 }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        arcColors.forEachIndexed { index, color ->
            RainbowArc(
                color = color,
                duration = duration,
                delay = (index + 1) * 100,
                radius = initialRadius + (arcSpacing + strokeWidth) * index, // 根据位置确定圆弧半径
                strokeWidth = strokeWidth,
                strokeCap = strokeCap
            )
        }
    }
}

@Composable
fun RainbowArc(
    color: Color,
    duration: Int,
    delay: Int,
    radius: Dp,
    strokeWidth: Dp,
    strokeCap: StrokeCap
) {
    // 无限旋转动画
    val infiniteTransition = rememberInfiniteTransition("InfiniteTransition")
    // 从0度到360度的动画
    val rotation by infiniteTransition.animateFloat(
        0f, 360f, infiniteRepeatable(
            keyframes {
                durationMillis = duration - delay
                delayMillis = delay
                //定义关键帧
                0f atFraction 0f
                400f atFraction 0.5f using FastOutLinearInEasing
                360f atFraction 1f using LinearOutSlowInEasing
            }
        ),
        label = "RainbowArcRotation"
    )

    Canvas(
        modifier = Modifier
            .size(radius * 2)
            .rotate(rotation)
    ) {
        drawArc(
            color = color,
            startAngle = -180f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = strokeCap)
        )
    }
}