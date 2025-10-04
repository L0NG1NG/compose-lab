package com.longing.awesomeview.ui.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.longing.awesomeview.ui.components.AnimatedGenderSign
import com.longing.awesomeview.ui.components.CircleLoader
import com.longing.awesomeview.ui.components.GenderSign
import com.longing.awesomeview.ui.components.RainbowLoader
import com.longing.awesomeview.ui.components.TypewriteText
import com.longing.awesomeview.ui.components.convexBorder
import com.longing.awesomeview.ui.theme.Purple
import com.longing.awesomeview.ui.theme.Purple40

@Composable
fun HomeScreen() {
    var isStart by remember { mutableStateOf(true) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val (panel, startButton) = createRefs()

        Column(
            modifier = Modifier.constrainAs(panel) {
                top.linkTo(parent.top)
                start.linkTo(panel.start)
                end.linkTo(parent.end)
                bottom.linkTo(startButton.top)
                height = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RainbowLoader(
                    arcColors = listOf(
                        Color(0xFFFF0000), // 红
                        Color(0xFFFFA500), // 橙
                        Color(0xFFFFFF00), // 黄
                        Color(0xFF008000), // 绿
                        Color(0xFF0000FF), // 蓝
                        Color(0xFF4B0082), // 靛
                        Color(0xFFEE82EE)  // 紫
                    ),
                )

                Box(contentAlignment = Alignment.Center) {
                    CircleLoader(
                        modifier = Modifier.size(80.dp),
                        color = Purple40,
                        secondColor = Purple40,
                        isVisible = isStart
                    )

                    CircleLoader(
                        modifier = Modifier.size(40.dp),
                        color = Purple40,
                        secondColor = null,
                        tailLength = 280f,
                        isVisible = isStart
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                AnimatedGenderSign(
                    modifier = Modifier.size(50.dp),
                    genderSign = if (isStart) GenderSign.MALE else GenderSign.FEMALE,
                    strokeWidth = 4.dp
                )

                Spacer(modifier = Modifier.width(16.dp))

                var text by remember { mutableStateOf("") }
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Search
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .size(350.dp, 60.dp)
                                // 设置背景颜色和形状
                                .background(Purple, CircleShape)
                                // 应用相同颜色和形状的凸起边框
                                .convexBorder(Purple, CircleShape)
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // 添加搜索图标
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null
                            )
                            Box {
                                // 当输入文本为空时显示占位符
                                if (text.isEmpty()) {
                                    Text(
                                        text = "Search...",
                                        style = LocalTextStyle.current.copy(color = Color(0xFF242424))
                                    )
                                }
                                // 显示实际的文本字段
                                innerTextField()
                            }
                        }
                    }
                )
            }
        }

        Button(modifier = Modifier.constrainAs(startButton) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, onClick = { isStart = !isStart }) {
            TypewriteText(text = if (isStart) "Stop" else "Start", speed = 60)
        }
    }
}