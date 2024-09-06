package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme

/**
 * Defines a dropdown menu linked to an OutlinedButton
 * @param items is a List os strimngs that are the menu items.
 * @param modifier is the standard modifier to allow modification of the
 * component.
 * @param fontSize is the size of the text font used in the OutlineButton.
 * @param itemClick is the code to be run when an item within the dropdown
 * menu is tapped.
 * @author Chris Loftus
 */
@Composable
fun ButtonSpinner(
    items: List<String>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    itemClick: (String) -> Unit = {}
) {
    var itemText by rememberSaveable {
        mutableStateOf(if (items.isNotEmpty()) items[0] else "")
    }
    var expanded by rememberSaveable { mutableStateOf(false) }

    OutlinedButton(
        modifier = modifier,
        onClick = {
            expanded = !expanded
        }
    ) {
        Text(
            text = itemText,
            fontSize = fontSize,
            modifier = Modifier.padding(end = 8.dp)
        )

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription =
            stringResource(R.string.dropdown_icon)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        )
        {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        expanded = false
                        itemText = it
                        itemClick(it)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun SpinnerPreview() {
    FAATheme(dynamicColor = false) {
        val items = listOf("Numbers", "One", "Two")
        ButtonSpinner(items = items)
    }
}