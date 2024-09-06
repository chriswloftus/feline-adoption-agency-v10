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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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
class AddCatTest {

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
    fun addNewCat_AssertCatAppearsInList(){
        // Start by tapping on the cats tab
        // We expand the tree since the cats tab is deeply embedded within the page and the
        // merged version of the tree hides it.
        val catsTabString = composeTestRule.activity.getString(R.string.cats)
        composeTestRule.onNodeWithContentDescription(
            catsTabString,
            useUnmergedTree = true
        ).assertExists().performClick()

        // Let's check that we have navigated to the cats page
        var currentDestination = navController.currentBackStackEntry?.destination?.route
        Truth.assertThat(currentDestination).isEqualTo(Screen.Cats.route)

        // Does the Cat FAB exist and is so tap it
        val addCatFABString = composeTestRule.activity.getString(R.string.add_cat)
        composeTestRule.onNodeWithContentDescription(addCatFABString)
            .assertExists().performClick()

        // Let's check that we have navigated to the AddCat page
        currentDestination = navController.currentBackStackEntry?.destination?.route
        Truth.assertThat(currentDestination).isEqualTo(Screen.AddCat.route)

        // Now check the the cat name input field exists and if it does add TEST CAT
        val catAddNameString = composeTestRule.activity.getString(R.string.cat_name)
        composeTestRule.onNodeWithText(catAddNameString)
            .assertExists().performTextInput("TEST CAT")

        // Check add cat FAB exists and click it
        val addCatString = composeTestRule.activity.getString(R.string.add_cat)
        composeTestRule.onNodeWithContentDescription(addCatString)
            .assertExists().performClick()

        // Check we're back in Cats screen
        currentDestination = navController.currentBackStackEntry?.destination?.route
        Truth.assertThat(currentDestination).isEqualTo(Screen.Cats.route)

        // Check that the list contains TEST CAT
        composeTestRule.onNodeWithText("TEST CAT").assertExists()
    }
}