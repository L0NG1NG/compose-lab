package com.longing.awesomeview.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle

@Composable
fun TypewriteText(
    modifier: Modifier = Modifier,
    text: String,
    speed: Int,
    isVisible: Boolean = true,
    style: TextStyle = LocalTextStyle.current,
    preoccupySpace: Boolean = true
) {
    val spec: AnimationSpec<Int> = tween(
        durationMillis = text.length * speed,
        easing = LinearEasing
    )

    // 存储当前动画显示的文本
    var textToAnimate by remember { mutableStateOf("") }

    // 控制动画进度的Animatable
    val index = remember {
        Animatable(initialValue = 0, typeConverter = Int.VectorConverter)
    }

    // 处理可见性变化的副作用
    LaunchedEffect(isVisible) {
        if (isVisible) {
            textToAnimate = text
            index.animateTo(text.length, spec)
        } else {
            index.snapTo(0)
        }
    }

    // 处理文本内容变化的副作用
    LaunchedEffect(text) {
        if (isVisible) {
            index.snapTo(0)
            textToAnimate = text
            index.animateTo(text.length, spec)
        }
    }

    // 包含动画文本和静态文本的Box
    Box(modifier = modifier) {
        if (preoccupySpace && index.isRunning) {
            // 预占空间的透明文本
            Text(
                text = text,
                style = style,
                modifier = Modifier.alpha(0f)
            )
        }

        // 根据当前index值显示动画文本
        Text(
            text = textToAnimate.substring(0, index.value),
            style = style
        )
    }
}