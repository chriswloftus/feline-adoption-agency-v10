package uk.ac.aber.dcs.cs31620.faa

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.aber.dcs.cs31620.faa.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

@RunWith(AndroidJUnit4::class)
class NavigationDrawerTest {

    // Partly based on the YouTube tutorial: https://www.youtube.com/watch?v=-G3kCDXe9Ro

    //@get:Rule
    //val composeTestRule = createComposeRule()

    // This version is useful if you need access to parts of the activity
    // ComponentActivity is a dummy activity inserted by the ui-test-manifest library
    //so that there are no conflicts with MainActivity
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var navController: NavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            FAATheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = TestNavHostController(LocalContext.current)
                    navController.navigatorProvider.addNavigator(ComposeNavigator())
                    BuildNavigationGraph(navController = navController)
                }
            }
        }
    }

    @Test
    fun assert_IsMainScreenDestinationRoute(){
        val currentDestination = navController.currentBackStackEntry?.destination?.route

        Truth.assertThat(currentDestination).isEqualTo(Screen.Home.route)
    }

    @Test
    fun clickOnLoginButton_OpensLoginPage() {

        val burgerButtonString = composeTestRule.activity.getString(R.string.nav_drawer_menu)

        // Start by opening the navigation drawer by clicking on the burger button
        composeTestRule.onNodeWithContentDescription(burgerButtonString)
            .assertExists().performClick()

        // We now check the login button exists and then click it to open the login page
        val loginButtonString = composeTestRule.activity.getString(R.string.login)
        composeTestRule.onNodeWithContentDescription(loginButtonString)
            .assertExists().performClick()

        // Let's check that we have navigated to the login page
        val currentDestination = navController.currentBackStackEntry?.destination?.route
        Truth.assertThat(currentDestination).isEqualTo(Screen.Login.route)

        // Now check that we have the login text field on that page
        val loginEmailString = composeTestRule.activity.getString(R.string.login_email)
        composeTestRule.onNodeWithText(loginEmailString).assertExists()

        // A useful debug tool, to show complete UI component tree (expanded) in LogCat.
        // Filter by faa_screen
        //composeTestRule.onRoot(useUnmergedTree = true).printToLog("faa_screen")

        // Might be useful during debugging to slow things down to show what is displayed
        //Thread.sleep(5000L)
    }
}