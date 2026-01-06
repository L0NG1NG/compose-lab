package com.longing.awesomeview.ui.shape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.hypot

class CircularHoleShape(
    private val progress: Float,
    private val center: TransformOrigin = TransformOrigin.Center
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val rect = size.toRect()
        if (progress == 0f) return Outline.Rectangle(rect) // 进度为0时显示完整矩形

        // 计算洞中心坐标
        val holeCenter = Offset(
            center.pivotFractionX * size.width,
            center.pivotFractionY * size.height
        )

        // 计算到四个角落的最大距离
        val targetRadius = maxOf(
            holeCenter distanceTo rect.topLeft,
            holeCenter distanceTo rect.topRight,
            holeCenter distanceTo rect.bottomLeft,
            holeCenter distanceTo rect.bottomRight
        )

        val holeRadius = targetRadius * progress // 根据进度缩放半径

        // 创建带洞的路径
        return Outline.Generic(Path().apply {
            op( // 路径布尔运算
                Path().apply { addRect(rect) }, // 原始矩形
                Path().apply { addOval(Rect(holeCenter, holeRadius)) }, // 圆形洞
                PathOperation.Difference // 差集运算
            )
        })

    }

    private infix fun Offset.distanceTo(other: Offset): Float {
        return hypot(other.x - this.x, other.y - this.y)
    }
}
