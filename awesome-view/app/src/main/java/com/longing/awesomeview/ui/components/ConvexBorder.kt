package com.longing.awesomeview.ui.components

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class ConvexStyle(
    val blur: Dp = 3.dp,
    val offset: Dp = 2.dp,
    val glareColor: Color = Color.White.copy(0.64f),
    val shadowColor: Color = Color.Black.copy(0.64f)
)

fun Modifier.convexBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 8.dp,
    convexStyle: ConvexStyle = ConvexStyle()
) = this.drawWithContent {
    // 调整大小以适应画布边界
    val adjustedSize = Size(size.width - strokeWidth.toPx(), size.height - strokeWidth.toPx())
    // 根据形状和调整后的大小创建轮廓
    val outline = shape.createOutline(adjustedSize, layoutDirection, this)

    // 绘制可组合内容的原始内容
    drawContent()

    // 平移画布以适应边界
    val halfStrokeWidth = strokeWidth.toPx() / 2
    translate(halfStrokeWidth, halfStrokeWidth) {
        // 绘制主边框轮廓
        drawOutline(
            outline = outline,
            color = color,
            style = Stroke(width = strokeWidth.toPx())
        )
    }

    with(convexStyle) {
        // 绘制阴影轮廓
        drawConvexBorderShadow(outline, strokeWidth, blur, -offset, -offset, shadowColor)
        // 绘制高光轮廓
        drawConvexBorderShadow(outline, strokeWidth, blur, offset, offset, glareColor)
    }
}


// 绘制创建凸起效果的阴影
private fun DrawScope.drawConvexBorderShadow(
    outline: Outline,
    strokeWidth: Dp,
    blur: Dp,
    offsetX: Dp,
    offsetY: Dp,
    shadowColor: Color
) = drawIntoCanvas { canvas ->
    // 创建并设置Paint对象
    val shadowPaint = Paint().apply {
        this.style = PaintingStyle.Stroke
        this.color = shadowColor
        this.strokeWidth = strokeWidth.toPx()
    }

    // 保存当前图层
    canvas.saveLayer(size.toRect(), shadowPaint)

    val halfStrokeWidth = strokeWidth.toPx() / 2
    // 平移画布以适应边界
    canvas.translate(halfStrokeWidth, halfStrokeWidth)
    // 绘制阴影轮廓
    canvas.drawOutline(outline, shadowPaint)

    // 应用混合模式和模糊效果
    shadowPaint.asFrameworkPaint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
    }

    // 设置裁剪颜色
    shadowPaint.color = Color.Black
    // 平移画布并绘制阴影裁剪轮廓
    canvas.translate(offsetX.toPx(), offsetY.toPx())
    canvas.drawOutline(outline, shadowPaint)
    // 恢复画布原始状态
    canvas.restore()
}