package uk.ac.aber.dcs.cs31620.faa

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.aber.dcs.cs31620.faa.ui.components.MainPageTopAppBar

@RunWith(AndroidJUnit4::class)
class MainPageTopAppBarTest {

    //@get:Rule
    //val composeTestRule = createComposeRule()

    @get:Rule
    val androidComposeTestRule = createComposeRule()

    @Test
    fun clickOnAndroidBurgerIcon_OpensNavigation() {
        androidComposeTestRule.setContent {
            MainPageTopAppBar()
        }

        Thread.sleep(1000L)

        androidComposeTestRule.onNodeWithContentDescription("Navigation drawer menu").assertExists()
    }
}