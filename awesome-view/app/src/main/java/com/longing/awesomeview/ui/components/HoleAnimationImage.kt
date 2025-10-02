package com.longing.awesomeview.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import com.longing.awesomeview.ui.shape.CircularHoleShape

@Composable
fun HoleAnimationImage(
    bitmap: ImageBitmap,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    animationSpec: AnimationSpec<Float> = tween(500),
    onFinish: ((Boolean) -> Unit)? = null
) {
    var holeCenter by remember { mutableStateOf(TransformOrigin.Center) }
    var isVisible by remember { mutableStateOf(true) }
    Image(
        bitmap,
        contentDescription,
        modifier.detectTapWithOrigin { origin ->
                holeCenter = origin
                isVisible = !isVisible // 点击切换状态
            }
            .holeAnimation(isVisible, holeCenter, animationSpec, onFinish),
        alignment,
        contentScale,
        alpha,
        colorFilter,
        filterQuality
    )
}

@Composable
private fun Modifier.holeAnimation(
    isVisible: Boolean, // 控制显示/隐藏
    center: TransformOrigin = TransformOrigin.Center,
    animationSpec: AnimationSpec<Float>,
    onFinish: ((Boolean) -> Unit)?
): Modifier {
    val progress by animateFloatAsState(
        targetValue = if (isVisible) 0f else 1f,
        animationSpec = animationSpec,
        label = "HoleProgress",
        finishedListener = {
            onFinish?.invoke(it == 0f)
        }
    )
    return hole(progress, center)
}

private fun Modifier.hole(
    progress: Float,
    center: TransformOrigin = TransformOrigin.Center
) = this.clip(CircularHoleShape(progress, center))

private fun Modifier.detectTapWithOrigin(
    onTap: (TransformOrigin) -> Unit
) = pointerInput(Unit) {
    detectTapGestures { offset ->
        onTap(
            TransformOrigin(
                offset.x / size.width,
                offset.y / size.height
            )
        )
    }
}