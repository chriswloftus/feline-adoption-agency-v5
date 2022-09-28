package uk.ac.aber.dcs.cs31620.faa.ui.cats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.components.ButtonSpinner
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme


/**
 * Represents the cats screen. For this version we have a search area that allows us to
 * choose cat breed, cat age, cat gender and how far we are willing to travel.
 * These are all OutlinedButtons sitting on a Card. The breed, age and gender are
 * implemented using dropdown menus from data hard coded in strings.xml.
 * The distance to travel is implemented an AlertDialog that has a Slider to
 * control how far we want to search.
 * Currently, the search data is not used to find suitable cats. That will happen
 * in a later workshop.
 * @author Chris Loftus
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatsScreen(
    navController: NavHostController
) {
    TopLevelScaffold(
        navController = navController
    )
    { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val breedList = stringArrayResource(id = R.array.breed_array).toList()
            val genderList = stringArrayResource(id = R.array.gender_array).toList()
            val ageList = stringArrayResource(id = R.array.age_range_array).toList()
            var selectedBreed by rememberSaveable { mutableStateOf(breedList[0]) }
            var selectedGender by rememberSaveable { mutableStateOf(genderList[0]) }
            var selectedAge by rememberSaveable { mutableStateOf(ageList[0]) }
            var proximity by rememberSaveable { mutableStateOf(10) }

            var dialogIsOpen by rememberSaveable { mutableStateOf(false) }

            Card(
                shape = RectangleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row {
                    ButtonSpinner(
                        items = breedList,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                        itemClick = { selectedBreed = it }
                    )

                    ButtonSpinner(
                        items = genderList,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, end = 8.dp),
                        itemClick = { selectedAge = it }
                    )
                }

                Row {
                    ButtonSpinner(
                        items = ageList,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                        itemClick = { selectedBreed = it }
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
                            text = stringResource(id = R.string.distance, proximity),
                            fontSize = 16.sp
                        )
                    }

                    DistanceDialog(
                        distance = proximity,
                        dialogIsOpen = dialogIsOpen,
                        dialogOpen = { isOpen ->
                            dialogIsOpen = isOpen
                        },
                        changeDistance = { newDistance ->
                            proximity = newDistance
                        })
                }
            }
        }
    }
}

@Composable
private fun DistanceDialog(
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

@Preview
@Composable
private fun CatsScreenPreview() {
    val navController = rememberNavController()
    FAATheme(dynamicColor = false) {
        CatsScreen(navController)
    }
}