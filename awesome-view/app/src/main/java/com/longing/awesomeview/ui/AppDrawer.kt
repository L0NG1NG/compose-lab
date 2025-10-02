package com.longing.awesomeview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.longing.awesomeview.R
import com.longing.awesomeview.ui.components.HoleAnimationImage

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    drawerItems: List<NavigationItem>,
    drawItemClicked: (item: NavigationItem) -> Unit,
) {
    ModalDrawerSheet(
        drawerState = drawerState,
        windowInsets = WindowInsets(0)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.img_drawer_banner_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            HoleAnimationImage(
                bitmap = ImageBitmap.imageResource(R.drawable.img_drawer_banner_2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(24.dp))
        LazyColumn {
            items(drawerItems.size) { index ->
                val item = drawerItems[index]
                NavigationDrawerItem(
                    label = { Text(text = item.title) },
                    icon = { Icon(item.icon, item.title) },
                    selected = currentRoute == item.route,
                    onClick = { drawItemClicked(item) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
}