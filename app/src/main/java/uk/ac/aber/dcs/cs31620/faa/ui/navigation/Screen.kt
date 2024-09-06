package uk.ac.aber.dcs.cs31620.faa.ui.navigation
/**
 * Wraps as objects, singletons for each screen used in
 * navigation. Each has a unique route.
 * @param route To pass through the route string
 * @author Chris Loftus
 */
sealed class Screen(
    val route: String
) {
    object Home : Screen("home")
    object Cats : Screen("cats")
    object Login : Screen("login")
    object AddCat : Screen("addCat")
}

val screens = listOf(
    Screen.Home,
    Screen.Cats
)
