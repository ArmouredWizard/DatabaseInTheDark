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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uk.co.maddwarf.databaseinthedark.screens.composables.BodyBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton


@Composable
fun DiceRollerDialog(
    onDismiss: () -> Unit,
    dice: Int,
    action: String
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(20.dp)),
              //  .clip(shape = RoundedCornerShape(20.dp))
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .padding(10.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "DICE ROLLER")
                BodyBlock(title = "", text = "Dice Roller Controls go here")
                BodyBlock(title = "", text = "You have a total of $dice Dice to roll for $action")
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MyButton(onClick = onDismiss, text = "Cancel")
                    MyButton(onClick = { /*TODO*/ }, text = "ROLL")
                }
            }
        }
    }
}