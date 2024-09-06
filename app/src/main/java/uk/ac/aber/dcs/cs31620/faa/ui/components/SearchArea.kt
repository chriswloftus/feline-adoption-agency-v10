package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.CatSearch

/**
 * A component that defines four search filters: breed, gender, age and
 * distance the adopter is willing to travel. These are OutlinedButtons
 * with DropDown menus.
 * @param modifier The standard modifier for styling the overall search Card
 * @param breedList A list of all the breeds to display
 * @param updateBreed The function to hoist the breed selected
 * @param genderList A list of all the genders to display
 * @param updateDender The function to hoist the gender selected
 * @param ageList A list of all the cat ages to display
 * @param updateAge The function to hoist the age selected
 * @param proximity The current distance the adopter is willing to travel
 * @param updateProximity The function to hoist the new distance the adopter is willing to travel
 */
@Composable
fun SearchArea(
    modifier: Modifier = Modifier,
    catSearch: CatSearch,
    breedList: List<String>,
    genderList: List<String>,
    ageList: List<String>,
    updateSearch: (CatSearch)-> Unit = {}
){

    var dialogIsOpen by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Row {
            ButtonSpinner(
                items = breedList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                itemClick = {
                    updateSearch(
                        CatSearch(
                            breed = it,
                            gender = catSearch.gender,
                            ageRange = catSearch.ageRange,
                            distance = catSearch.distance
                        )
                    )
                }
            )

            ButtonSpinner(
                items = genderList,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, end = 8.dp),
                itemClick = {
                    updateSearch(
                        CatSearch(
                            breed = catSearch.breed,
                            gender = it,
                            ageRange = catSearch.ageRange,
                            distance = catSearch.distance
                        )
                    )
                }
            )
        }

        Row {
            ButtonSpinner(
                items = ageList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                itemClick = {
                    updateSearch(
                        CatSearch(
                            breed = catSearch.breed,
                            gender = catSearch.gender,
                            ageRange = it,
                            distance = catSearch.distance
                        )
                    )
                }
            )

            OutlinedButton(
                onClick = {
                    // Changing the state will cause a recomposition of DistanceDialog
                    dialogIsOpen = true
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.distance, catSearch.distance),
                    fontSize = 16.sp
                )
            }

            DistanceDialog(
                distance = catSearch.distance,
                dialogIsOpen = dialogIsOpen,
                dialogOpen = { isOpen ->
                    dialogIsOpen = isOpen
                },
                changeDistance = {
                    updateSearch(
                        CatSearch(
                            breed = catSearch.breed,
                            gender = catSearch.gender,
                            ageRange = catSearch.ageRange,
                            distance = it
                        )
                    )
                }
            )
        }
    }
}