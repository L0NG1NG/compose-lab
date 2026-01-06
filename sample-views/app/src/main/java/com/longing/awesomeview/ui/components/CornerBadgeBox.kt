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
    object SideBackBadge2 : LayoutId()
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
                    .layoutId(LayoutId.SideBackBadge2)
                    .scale(corner.layoutScaleX, corner.layoutScaleY)
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(1f, 1f)
                        rotationZ = 45f
                    }
                    .background(backBadgeBrush, badgeShape)
            )

            Box(
                Modifier
                    .layoutId(LayoutId.SideFrontBadge)
                    .scale(corner.layoutScaleX, corner.layoutScaleY) // 根据角位置翻转
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 1f) // 设置旋转中心
                        rotationZ = -45f
                    }
                    .background(badgeBrush, badgeShape)
            )

            Box(
                modifier = Modifier
                    .layoutId(LayoutId.BadgeContent)
                    .scale(corner.innerScaleX, corner.innerScaleY)
                    .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 1f)
                        rotationZ = -45f
                        // 如果 innerScaleX 或 innerScaleY 是 -1f，这里将其再乘以 -1f 变回 1f
//                        scaleX = corner.innerScaleX
//                        scaleY = corner.innerScaleY

                },
                content = badgeContent,
                contentAlignment = Alignment.Center
            )

            Box(
                modifier = Modifier.layoutId(LayoutId.MainContent),
                content = content
            )

        }
    ) { measurables, constraints ->

        val mainContentPadding = contentPadding.toPx().roundToInt() + cornerPadding.roundToPx()
        // 为主内容创建一个新的约束，减去内边距
        val mainConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = constraints.maxWidth - mainContentPadding * 2,
            maxHeight = constraints.maxHeight - mainContentPadding * 2
        )
        val mainPlaceable = measurables.first { it.layoutId == LayoutId.MainContent }.measure(mainConstraints)

        // 2. 计算布局的最终尺寸
        val width: Int
        val height: Int
        if (constraints.hasBoundedWidth) {
            width = constraints.maxWidth
            height = constraints.maxHeight
        } else {
            width = mainPlaceable.width + mainContentPadding * 2
            height = mainPlaceable.height + mainContentPadding * 2
        }

        // 3. 测量角标条 (Badge)
        val badgeSize = (squareDiagonal(stripThickness + contentPadding) + cornerPadding).roundToPx()
        val badgeConstraints = constraints.copy(
            minWidth = badgeSize,
            minHeight = stripThickness.roundToPx(),
        )

        val backBadgePlaceable = measurables.first { it.layoutId == LayoutId.SideBackBadge }
            .measure(badgeConstraints)
        val backBadgePlaceable2 = measurables.first { it.layoutId == LayoutId.SideBackBadge2 }
            .measure(badgeConstraints)

        val frontBadgePlaceable = measurables.first { it.layoutId == LayoutId.SideFrontBadge }
            .measure(badgeConstraints)
        val badgeContentPlaceable = measurables.first { it.layoutId == LayoutId.BadgeContent }
            .measure(badgeConstraints)


        // 5. 开始布局 (layout)
        layout(width, height) {

            val (baseX, baseY) = corner.alignment.align(
                size = IntSize.Zero,
                space = IntSize(mainPlaceable.width, mainPlaceable.height),
                layoutDirection = layoutDirection
            )

            backBadgePlaceable.placeRelative(
                mainContentPadding + baseX - frontBadgePlaceable.width + contentPadding.roundToPx(),
                -contentPadding.roundToPx() + baseY + cornerPadding.roundToPx())

            backBadgePlaceable2.placeRelativeWithLayer(
                mainContentPadding + baseX - frontBadgePlaceable.width + contentPadding.roundToPx(),
                -contentPadding.roundToPx() + baseY + stripThickness.roundToPx() + cornerPadding.roundToPx()

            )

            // 放置主内容，并应用内边距
            mainPlaceable.placeRelative(mainContentPadding, mainContentPadding)

            // 放置 Front Badge 和 Badge Content 的基准 (左上角)
            val frontBadgeX = mainContentPadding + baseX - frontBadgePlaceable.width + contentPadding.roundToPx()
            val frontBadgeY = -contentPadding.roundToPx() + baseY + stripThickness.roundToPx() + cornerPadding.roundToPx()
            frontBadgePlaceable.placeRelative(frontBadgeX, frontBadgeY)
            badgeContentPlaceable.placeRelative(frontBadgeX, frontBadgeY)
        }
    }
}

