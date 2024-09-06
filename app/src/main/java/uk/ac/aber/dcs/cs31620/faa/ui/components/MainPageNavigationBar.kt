package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.screens
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

/**
 * Creates the navigation bar displayed at the bottom of the screen.
 * Currently has just two items: the Home item and the Cats item.
 * @param navController To pass through the NavHostController since navigation is required
 * @author Chris Loftus
 */
@Composable
fun MainPageNavigationBar(
    navController: NavHostController
) {
    val icons = mapOf(
        Screen.Home to IconGroup(
            filledIcon = Icons.Filled.Home,
            outlineIcon = Icons.Outlined.Home,
            label = stringResource(id = R.string.home)
        ),
        Screen.Cats to IconGroup(
            filledIcon = Icons.Filled.Pets,
            outlineIcon = Icons.Outlined.Pets,
            label = stringResource(id = R.string.cats)
        )
    )

    NavigationBar {
        val navBackStackEntry by
        navController.currentBackStackEntryAsState()
        val currentDestination =
            navBackStackEntry?.destination
        screens.forEach { screen ->
            // In our case we know this simple version will work
            val isSelected = currentDestination?.route == screen.route
            val labelText = icons[screen]!!.label

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = (
                                if (isSelected)
                                    icons[screen]!!.filledIcon
                                else
                                    icons[screen]!!.outlineIcon),
                        contentDescription = labelText
                    )
                },
                label = { Text(labelText) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true // Save state of all destinations popped off stack
                            //inclusive = true // This would also remove Home from stack if navigating to Cats
                        }
                        // launchSingleTop: Navigate to the "search” destination only if we’re not already on
                        // the "search" destination, avoiding multiple copies on the top of the
                        // back stack
                        launchSingleTop = true
                        // If there was state previously saved for this destination
                        // when it was popped we can now get it back
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun MainPageNavigationBarPreview() {
    val navController = rememberNavController()
    FAATheme(dynamicColor = false) {
        MainPageNavigationBar(navController)
    }
}