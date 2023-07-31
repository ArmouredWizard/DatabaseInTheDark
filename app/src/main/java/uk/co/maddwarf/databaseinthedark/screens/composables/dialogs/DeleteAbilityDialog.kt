package uk.co.maddwarf.databaseinthedark.screens.composables.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock

@Composable
fun DeleteAbilityDialog(
    ability:String,
    onAccept:(String) -> Unit,
    onDismiss:() -> Unit
){
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(20.dp))
                .clip(shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    // .border(shape = RoundedCornerShape(20.dp), color = Color.DarkGray, width = 2.dp)
                    .padding(10.dp)
                //  .clip(shape = RoundedCornerShape(20.dp)),
                ,horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleBlock(title = "Ability:", text = ability)
                Text(text = "Are you sure you wish to remove this Ability?")
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MyButton(onClick = { onAccept(ability) }, text = "Yes")
                    MyButton(onClick = onDismiss, text = "No")
                }
            }
        }

    }
}