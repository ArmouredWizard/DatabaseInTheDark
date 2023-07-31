package uk.co.maddwarf.databaseinthedark.screens.composables.dialogs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.screens.composables.CustomSpinner
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryRowWithInfoIcon
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock

@Composable
fun ContactEntryDialog(
    contactName: String,
    onDismiss: () -> Unit,
    onAccept: (String, Rank) -> Unit,
    onValueChange: (String) -> Unit,
    contactList: List<String>
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(20.dp))
                .clip(shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleBlock(title = "", text = "Add new Contact")
                TextEntryRowWithInfoIcon(
                    data = contactName,
                    onValueChange = onValueChange,
                    label = "New Contact",
                    infoText = "Enter a name for this Contact" //todo improve info text
                )
                var contactExpanded by remember { mutableStateOf(false) }
                var chosenContactName by remember { mutableStateOf("") }
                fun contactChooser(contact: String) {
                    contactExpanded = false
                    chosenContactName = contact
                    onValueChange(chosenContactName)
                }
                CustomSpinner(   //todo prevent duplicate Contacts
                    expanded = contactExpanded,
                    onClick = { contactExpanded = !contactExpanded },
                    list = contactList,
                    chooser = ::contactChooser,
                    report = "Chose Contact"
                )
                Text(text = "Enter Rating")
                val radioOptions = listOf("Foe", "Neutral", "Friend")
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
                var rank: Rank by remember { mutableStateOf(Rank.NEUTRAL) }
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(26.dp)
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                        rank = when (text) {
                                            "Foe" -> Rank.BAD
                                            // "Neutral" -> Rank.NEUTRAL
                                            "Friend" -> Rank.GOOD
                                            else -> Rank.NEUTRAL
                                        }
                                        Log.d("NEW RANK", rank.rankName)
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null // null recommended for accessibility with screenreaders
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }//todo make this work

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MyButton(onClick = onDismiss, text = "Cancel")
                    MyButton(onClick = { onAccept(contactName, rank) }, text = "Accept")
                }//end button Row
            }//end column
        }//end surface
    }//end dialog
}