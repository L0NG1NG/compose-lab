package com.longing.awesomeview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.longing.awesomeview.ui.shape.CornerBadgeShape
import kotlin.math.roundToInt
import kotlin.math.sqrt


sealed class BadgeCorner(
    val alignment: Alignment,
    val layoutScaleX: Float,
    val layoutScaleY: Float,
    val innerScaleX: Float,
    val innerScaleY: Float
) {
    object TopLeft : BadgeCorner(Alignment.TopStart, 1f, 1f, 1f, 1f)
    object TopRight : BadgeCorner(Alignment.TopEnd, -1f, 1f, -1f, 1f)
    object BottomLeft : BadgeCorner(Alignment.BottomStart, 1f, -1f, 1f, -1f)
    object BottomRight : BadgeCorner(Alignment.BottomEnd, -1f, -1f, -1f, -1f)
}

private sealed class LayoutId {
    object BadgeContent : LayoutId()
    object SideFrontBadge : LayoutId()
    object SideBackBadge : LayoutId()
    object MainContent : LayoutId()
}

private fun squareDiagonal(side: Dp): Dp {
    return squareDiagonal(side.value).dp
}

private fun squareDiagonal(side: Float): Float {
    return side * sqrt(2f)
}

@Composable
fun CornerBadgeBox(
    badgeBrush: Brush,
    backBadgeBrush: Brush,
    modifier: Modifier = Modifier,
    corner: BadgeCorner = BadgeCorner.TopRight,
    contentPadding: Dp = 4.dp,
    stripThickness: Dp = 32.dp,
    cornerPadding: Dp = 32.dp,
    cornerRoundness: Float = 0.5f,
    badgeContent: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val badgeShape = remember(cornerRoundness) { CornerBadgeShape(cornerRoundness) }
    Layout(
        modifier = modifier,
        content = {
            Box(
                Modifier
                    .layoutId(LayoutId.SideBackBadge)
                    .scale(corner.layoutScaleX, corner.layoutScaleY)
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 1f)
                        rotationZ = 45f
                        scaleY = -1f
                    }
                    .background(backBadgeBrush, badgeShape)
            )

            Box(
                Modifier
                    .layoutId(LayoutId.SideFrontBadge)
                    .scale(corner.layoutScaleX, corner.layoutScaleY) // 根据角位置翻转
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 1f) // 设置旋转中心
                        rotationZ = 45f
                    }
                    .background(badgeBrush, badgeShape)
            )

            Box(
                modifier = Modifier
                    .layoutId(LayoutId.BadgeContent)
                    .scale(corner.innerScaleX, corner.innerScaleY), // 翻转内容以适应角标
                content = badgeContent,
                contentAlignment = Alignment.Center
            )

            Box(
                modifier = Modifier.layoutId(LayoutId.MainContent),
                content = content
            )

        }
    ) { measurables, constraints ->

        val mainContentPadding = contentPadding.toPx().roundToInt()
        // 为主内容创建一个新的约束，减去内边距
        val mainConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = constraints.maxWidth - mainContentPadding * 2,
            maxHeight = constraints.maxHeight - mainContentPadding * 2
        )
        val mainPlaceable = measurables.first { it.layoutId == LayoutId.MainContent }.measure(mainConstraints)

        // 2. 计算布局的最终尺寸
        val width = if (constraints.hasBoundedWidth) constraints.maxWidth else mainPlaceable.width + mainContentPadding * 2
        val height = if (constraints.hasBoundedHeight) constraints.maxHeight else mainPlaceable.height + mainContentPadding * 2

        // 3. 测量角标条 (Badge)
        val badgeSize = (squareDiagonal(stripThickness + contentPadding)
                + cornerPadding).roundToPx()

        val badgeConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = badgeSize,
            maxHeight = badgeSize
        )
        val frontBadgePlaceable =
            measurables.first { it.layoutId == LayoutId.SideFrontBadge }.measure(badgeConstraints)
        val backBadgePlaceable =
            measurables.first { it.layoutId == LayoutId.SideBackBadge }.measure(badgeConstraints)

        // 4. 测量角标上的内容 (BadgeContent)
        val stripThicknessPx = stripThickness.toPx()
        val cornerPaddingPx = cornerPadding.toPx()
        // 角标内容的可用宽度约等于 (斜边长度 - 2 * 角落内边距) / sqrt(2)
        val badgeContentWidth = ((width * 1.414f - cornerPaddingPx * 2) / 1.414f).roundToInt()
        val badgeContentConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = badgeContentWidth,
            maxHeight = stripThicknessPx.roundToInt()
        )
        val badgeContentPlaceable =
            measurables.first { it.layoutId == LayoutId.BadgeContent }.measure(badgeContentConstraints)

        // 5. 开始布局 (layout)
        layout(width, height) {
            // 放置主内容，并应用内边距
            mainPlaceable.placeRelative(mainContentPadding, mainContentPadding)
            val groupPosition = corner.alignment.align(
                size = IntSize.Zero,
                space = IntSize(width, height),
                layoutDirection = layoutDirection
            )

            backBadgePlaceable.placeRelative(groupPosition)
            badgeContentPlaceable.placeRelative(groupPosition)
            frontBadgePlaceable.placeRelative(groupPosition)

        }
    }
}

