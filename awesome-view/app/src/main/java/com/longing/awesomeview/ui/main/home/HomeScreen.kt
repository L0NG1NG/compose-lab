package com.longing.awesomeview.ui.main.home

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.longing.awesomeview.ui.components.AnimatedGenderSign
import com.longing.awesomeview.ui.components.CircleLoader
import com.longing.awesomeview.ui.components.GenderSign

@Composable
fun HomeScreen() {
    var isStart by remember { mutableStateOf(true) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val (circleLoaderOuter, circleLoaderInner, startButton, genderSign) = createRefs()
        CircleLoader(
            modifier = Modifier
                .constrainAs(circleLoaderOuter) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(80.dp),
            color = MaterialTheme.colorScheme.primary,
            secondColor = MaterialTheme.colorScheme.secondary,
            isVisible = isStart)

        CircleLoader(
            modifier = Modifier
                .constrainAs(circleLoaderInner) {
                    top.linkTo(circleLoaderOuter.top)
                    start.linkTo(circleLoaderOuter.start)
                    bottom.linkTo(circleLoaderOuter.bottom)
                    end.linkTo(circleLoaderOuter.end)
                }
                .size(40.dp),
            color = MaterialTheme.colorScheme.tertiary,
            secondColor = null,
            tailLength = 280f,
            isVisible = isStart)

        AnimatedGenderSign(
            modifier = Modifier
                .constrainAs(genderSign) {
                    top.linkTo(circleLoaderOuter.top)
                    start.linkTo(circleLoaderOuter.end, margin = 8.dp)
                    bottom.linkTo(circleLoaderOuter.bottom)

                }
                .size(80.dp),
            genderSign = if (isStart) GenderSign.MALE else GenderSign.FEMALE,
            strokeWidth = 6.dp
        )

        Button(modifier = Modifier.constrainAs(startButton) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, onClick = { isStart = !isStart }) {
            Text(text = if (isStart) "Stop" else "Start")
        }
    }
}