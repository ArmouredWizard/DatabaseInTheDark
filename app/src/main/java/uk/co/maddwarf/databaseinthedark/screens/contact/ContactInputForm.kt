package uk.co.maddwarf.databaseinthedark.screens.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryRowWithInfoIcon

@Composable
fun ContactInputForm(
    modifier: Modifier = Modifier,
    onValueChange: (ContactDetails)->Unit,
    contactDetails: ContactDetails
    ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        TextEntryRowWithInfoIcon(
            data = contactDetails.name,
            onValueChange = {
                if (it.length <= 19)
                    onValueChange(contactDetails.copy(name = it))
            },
            label = "Name",
            infoText = "Enter the Contact's Name.\n" +
                    "This can be a Nickname, Moniker, Handle or anything that identifies them."
        )






    }//end Column
}//end InputForm