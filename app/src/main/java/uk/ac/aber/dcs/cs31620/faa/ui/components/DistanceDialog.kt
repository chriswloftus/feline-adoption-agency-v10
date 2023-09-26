package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs31620.faa.R

@Composable
fun DistanceDialog(
    distance: Int,
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    changeDistance: (Int) -> Unit = {}
) {

    var sliderPosition by rememberSaveable { mutableStateOf(distance.toFloat()) }

    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { /* Empty so clicking outside has no effect */ },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Map,
                    contentDescription = stringResource(R.string.distance_icon)
                )
            },
            title = {
                Text(text = stringResource(R.string.distance_to_travel_title))
            },
            text = {
                Column {
                    Text(stringResource(R.string.distance_to_travel_dialog_instructions))

                    // Add slider of distance to travel
                    Slider(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp),
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        valueRange = 0f..100f
                    )

                    Divider()

                    Text(
                        text = stringResource(id = R.string.distance, sliderPosition.toInt()),
                        fontSize = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        // Update the distance to the slider value
                        changeDistance(sliderPosition.toInt())
                    }
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        // Use the original distance
                        changeDistance(distance)
                    }
                ) {
                    Text(stringResource(R.string.dismiss_button))
                }
            }
        )
    }
}