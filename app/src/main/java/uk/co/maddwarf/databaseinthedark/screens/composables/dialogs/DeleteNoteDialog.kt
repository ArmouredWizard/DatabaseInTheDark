package uk.co.maddwarf.databaseinthedark.screens.composables.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.co.maddwarf.databaseinthedark.model.NoteDetails


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteNoteDialog(
    note: NoteDetails,
    onDismiss: () -> Unit,
    onAccept: (NoteDetails) -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(20.dp)
            ),
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(
                    text = "NO!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAccept(note) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(
                    text = "Yes Please!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray
                )
            }
        },
        title = { Text(text = note.title) },
        text = { Text(text = "Are you sure you wish to delete this Note?") }
    )
}