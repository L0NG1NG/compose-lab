package com.longing.awesomeview.ui.shape

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * https://mp.weixin.qq.com/s/6rW6jz2hxlV9kalPk6s8yw
 */
class CornerBadgeShape(
    @FloatRange(0.0, 1.0)
    private val cornerRoundness: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val (width, height) = size
        val cornerLength = computeAdjustedCornerLength(width, height, cornerRoundness)

        val path = Path().apply {
            moveTo(0f, height)
            lineTo(x = height - cornerLength / 2, y = cornerLength / 2)
            cubicTo(
                x1 = height, y1 = 0f,
                x2 = height + cornerLength / 3, y2 = 0f,
                x3 = height + cornerLength, y3 = 0f
            )
            lineTo(x = width - height - cornerLength, y = 0f)
            cubicTo(
                x1 = width - height - cornerLength / 3, y1 = 0f,
                x2 = width - height, y2 = 0f,
                x3 = width - height + cornerLength / 2, y3 = cornerLength / 2
            )
            lineTo(x = width, height)
            cubicTo(
                x1 = width - cornerLength / 2, y1 = height - cornerLength / 2,
                x2 = width - height * 0.66f, y2 = height - cornerLength / 2,
                x3 = width - height, y3 = height - cornerLength / 2
            )
            lineTo(x = height, y = height - cornerLength / 2)
            cubicTo(
                x1 = height * 0.66f, y1 = height - cornerLength / 2,
                x2 = cornerLength / 2, y2 = height - cornerLength / 2,
                x3 = 0f, y3 = height
            )
            close()
        }
        return Outline.Generic(path)
    }

    companion object {
        private const val BASE_CORNER_RATIO = 1 / 3f

        fun computeAdjustedCornerLength(width: Float, height: Float, cornerRoundness: Float): Float {
            val targetCornerLength = height * BASE_CORNER_RATIO * cornerRoundness.coerceIn(0f, 1f)
            val maxCornerLength = computeMaxCornerFitLength(width, height).coerceAtLeast(0f)
            return targetCornerLength.coerceAtMost(maxCornerLength)
        }

        private fun computeMaxCornerFitLength(width: Float, height: Float): Float {
            return (width - height * 2) / 2
        }
    }
}