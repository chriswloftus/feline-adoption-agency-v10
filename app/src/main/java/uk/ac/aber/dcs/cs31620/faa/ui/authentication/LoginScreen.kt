package uk.ac.aber.dcs.cs31620.faa.ui.authentication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

/**
 * Example login screen that is required for the Workshop 8
 * exercise. This is a start but would of course require
 * the behaviour (e.g. to Firebase authentication) to be implemented
 * and the inclusion of a Register button.
 * @param navController To pass through the NavHostController since navigation will be required
 * @author Chris Loftus
 */
@Composable
fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        val (emailField, passwordField, cancelButton, loginButton) = createRefs()

        OutlinedTextField(
            value = "",
            label = {
                Text(text = stringResource(R.string.login_email))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .constrainAs(emailField) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        OutlinedTextField(
            value = "",
            label = {
                Text(text = "Password:")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .constrainAs(passwordField) {
                    start.linkTo(parent.start)
                    top.linkTo(emailField.bottom)
                }
        )

        Button(
            onClick = { },
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(loginButton) {
                    start.linkTo(parent.start)
                    top.linkTo(passwordField.bottom)
                }
        ) {
            Text(stringResource(id = R.string.login))
        }

        Button(
            onClick = { },
            modifier = Modifier
                .constrainAs(cancelButton) {
                    start.linkTo(loginButton.end)
                    top.linkTo(passwordField.bottom)
                }
        ) {
            Text(stringResource(R.string.cancel))
        }
    }
}

@Composable
@Preview
private fun LoginScreenPreview() {
    val navController = rememberNavController()
    FAATheme(dynamicColor = false) {
        LoginScreen(navController)
    }
}