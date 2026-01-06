package com.longing.awesomeview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.longing.awesomeview.ui.main.MainScreen
import com.longing.awesomeview.ui.theme.AwesomeViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { AwesomeViewTheme { MainScreen() } }
    }
}