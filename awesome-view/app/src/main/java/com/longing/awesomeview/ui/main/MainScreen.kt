package com.longing.awesomeview.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.longing.awesomeview.ui.AppDrawer
import com.longing.awesomeview.ui.AwesomeViewNavGraph
import com.longing.awesomeview.ui.NavigationItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // 监听导航栈变化，确保路由状态能触发重组
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavigationItems.Home.route

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var selectedItem by remember { mutableStateOf(NavigationItems.Home) }

    val coroutineScope = rememberCoroutineScope()

    val drawerItems = listOf(
        NavigationItems.Home,
        NavigationItems.Feature1
    )

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                drawerItems = drawerItems,
                drawItemClicked = { drawerItem ->
                    navController.navigate(drawerItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                    selectedItem = drawerItem

                    coroutineScope.launch { drawerState.close() }
                })
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row (Modifier.fillMaxWidth().padding(end = 32.dp),Arrangement.Center){
                            Text(text = selectedItem.title)
                        }
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).clickable {
                                coroutineScope.launch { drawerState.open() }
                            }
                        )
                    }
                )
            }
        ) { paddingValues ->
            AwesomeViewNavGraph(Modifier.padding(paddingValues), navController)
        }
    }
}


object NavigationItems {
    val Home = NavigationItem(
        title = "来自Kotlin社区",
        icon = Icons.Default.Home,
        route = "home"
    )

    val Feature1 = NavigationItem(
        title = "todo",
        icon = Icons.Default.FavoriteBorder,
        route = "todo"
    )
}