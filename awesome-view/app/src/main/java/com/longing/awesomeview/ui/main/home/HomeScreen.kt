package com.longing.awesomeview.ui.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.longing.awesomeview.ui.components.AnimatedGenderSign
import com.longing.awesomeview.ui.components.CircleLoader
import com.longing.awesomeview.ui.components.GenderSign
import com.longing.awesomeview.ui.components.RainbowLoader
import com.longing.awesomeview.ui.components.TypewriteText

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
                Box(contentAlignment = Alignment.Center) {
                    CircleLoader(
                        modifier = Modifier.size(80.dp),
                        color = MaterialTheme.colorScheme.primary,
                        secondColor = MaterialTheme.colorScheme.secondary,
                        isVisible = isStart
                    )

                    CircleLoader(
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        secondColor = null,
                        tailLength = 280f,
                        isVisible = isStart
                    )
                }

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
            }
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceAround) {
                AnimatedGenderSign(
                    modifier = Modifier.size(50.dp),
                    genderSign = if (isStart) GenderSign.MALE else GenderSign.FEMALE,
                    strokeWidth = 4.dp
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