package uk.co.maddwarf.databaseinthedark.screens.composables.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uk.co.maddwarf.databaseinthedark.screens.composables.CustomSpinner
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryRowWithInfoIcon
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock

@Composable
fun AbilityEntryDialog(
    ability: String,
    onDismiss: () -> Unit,
    onAccept: (String) -> Unit,
    onValueChange: (String) -> Unit,
    abilityList: List<String>
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface (
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(20.dp))
                .clip(shape = RoundedCornerShape(20.dp))
        ){
            Column(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleBlock(title = "", text = "Add new Special Ability")
                TextEntryRowWithInfoIcon(
                    data = ability,
                    onValueChange = onValueChange,
                    label = "New Ability",
                    infoText = "A Special Ability" //todo improve info text
                )
                var abilityExpanded by remember{ mutableStateOf(false) }
                var chosenAbility by remember { mutableStateOf("") }
                fun abilityChooser(ability: String) {
                    abilityExpanded = false
                    chosenAbility = ability
                    onValueChange(chosenAbility)
                }
                CustomSpinner(   //todo prevent duplicate abilities
                    expanded = abilityExpanded,
                    onClick = { abilityExpanded = !abilityExpanded},
                    list = abilityList,
                    chooser = ::abilityChooser,
                    report = "Chose Special Ability"
                )

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MyButton(onClick = onDismiss, text = "Cancel")
                    MyButton(onClick = { onAccept(ability) }, text = "Accept")
                }//end button Row
            }//end column
        }//end surface
    }//end dialog
}//end AbilityEntryDialog