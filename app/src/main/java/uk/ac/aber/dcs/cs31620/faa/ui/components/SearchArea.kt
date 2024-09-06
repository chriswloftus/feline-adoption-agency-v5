package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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

@Composable
fun SearchArea(
    modifier: Modifier = Modifier,
    breedList: List<String>,
    updateBreed: (String)-> Unit,
    genderList: List<String>,
    updateGender: (String)-> Unit,
    ageList: List<String>,
    updateAge: (String)-> Unit,
    proximity: Int,
    updateProximity: (Int)-> Unit
){

    var dialogIsOpen by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RectangleShape,
        elevation =
        CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        // First row
        Row {
            ButtonSpinner(
                items = breedList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                itemClick = updateBreed
            )

            ButtonSpinner(
                items = genderList,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, end = 8.dp),
                itemClick = updateGender
            )
        }

        // Second row
        Row {
            ButtonSpinner(
                items = ageList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                itemClick = updateAge
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
                changeDistance = updateProximity
            )
        }
    }
}