package com.longing.awesomeview.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class GenderSign { MALE, FEMALE }

data class GenderSignColors(
    val maleSignColor: Color = Color.Blue,
    val femaleSignColor: Color = Color.Red
)

@Composable
fun AnimatedGenderSign(
    modifier: Modifier = Modifier,
    genderSign: GenderSign,
    strokeWidth: Dp = 2.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
    colors: GenderSignColors = GenderSignColors(),
    animationDuration: Int = 350,
) {
    val progress by animateFloatAsState(
        targetValue = if (genderSign == GenderSign.MALE) 1f else 0f,
        animationSpec = tween(animationDuration)

    )
    val color by animateColorAsState(
        targetValue = if (genderSign == GenderSign.MALE) colors.maleSignColor else colors.femaleSignColor,
        animationSpec = tween(animationDuration)
    )

    Canvas(modifier = modifier.size(24.dp)) {
        val height = size.height
        val width = size.width

        val circleRadius = height * 0.24f
        val padding = height * 0.1f
        // 计算中心点位置
        val circleCenter = Offset(
            x = width / 2,
            y = circleRadius + padding + ((height / 2 - padding * 2) * progress)
        )

        // 创建性别符号
        val genderSignPath = Path().apply {
            // 添加圆形
            addOval(Rect(circleCenter, circleRadius))
            moveTo(x = circleCenter.x, y = circleCenter.y + circleRadius)
            lineTo(x = circleCenter.x, y = circleCenter.y + circleRadius * 2)
            // 添加箭头/十字线
            moveTo(x = circleCenter.x - circleRadius * 0.4f, y = circleCenter.y + circleRadius * 1.6f)
            lineTo(
                x = circleCenter.x,
                y = circleCenter.y + circleRadius * 1.6f + (circleRadius * 0.4f * progress)
            )
            lineTo(x = circleCenter.x + circleRadius * 0.4f, y = circleCenter.y + circleRadius * 1.6f)
        }

        rotate(-135f * progress, circleCenter) {
            drawPath(
                path = genderSignPath,
                color = color,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = strokeCap
                )
            )
        }
    }
}