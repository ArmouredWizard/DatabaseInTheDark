package uk.co.maddwarf.databaseinthedark.screens.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.model.NoteDetails
import uk.co.maddwarf.databaseinthedark.screens.composables.TextEntryRowWithInfoIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputForm(
    noteDetails: NoteDetails,
    onValueChange: (NoteDetails) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextEntryRowWithInfoIcon(
            data = noteDetails.title,
            onValueChange = { onValueChange(noteDetails.copy(title = it)) },
            label = "Title",
            infoText = "Choose a Title for your Note.\n" +
                    "This will help you to find and identify it."
        )
        TextEntryRowWithInfoIcon(
            data = noteDetails.category,
            onValueChange = { onValueChange(noteDetails.copy(category = it)) },
            label = "Category",
            infoText = "Choose a Category for your Note.\n" +
                    "This can be used to filter similar Notes."
        )
        TextEntryRowWithInfoIcon(
            data = noteDetails.bodyText,
            onValueChange = { onValueChange(noteDetails.copy(bodyText = it)) },
            label = "Body Text",
            infoText = "The Main text of your Note.\n" +
                    "Place all relevant information here.",
            singleLine = false,
            limited = false
        )
    }
}//end NoteEntryForm