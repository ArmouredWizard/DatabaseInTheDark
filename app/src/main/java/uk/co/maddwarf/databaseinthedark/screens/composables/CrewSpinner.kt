package uk.co.maddwarf.databaseinthedark.screens.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.co.maddwarf.databaseinthedark.data.Crew

@Composable
fun CrewSpinner(
    expanded: Boolean,
    onClick: () -> Unit,
    list: List<Crew>,
    chooser: (Crew) -> Unit,
    report: Crew
){
    Box(
        modifier = Modifier
    ) {
        MyButton(text = ""/*report.name*/, onClick = onClick, trailingIcon = Icons.Filled.ArrowDropDown)
        //DropDown
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = (onClick),
            modifier = Modifier
                .background(color = Color.LightGray)
                .border(width = 2.dp, shape = RoundedCornerShape(5.dp), color = Color.DarkGray)
        ) {
            list.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = { chooser(itemValue) },
                    text = {
                        Text(
                            text = itemValue.crewName,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    })
            }//end list for each
        }//end dropdown menu
    }//end box
}
