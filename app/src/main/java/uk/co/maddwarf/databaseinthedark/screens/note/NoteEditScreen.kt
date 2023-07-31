package uk.co.maddwarf.databaseinthedark.screens.note

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.model.NoteDetails
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteNoteDialog

object NoteEditDestination : NavigationDestination {
    override val route = "note_edit"
    override val titleRes: Int = R.string.note_edit_title

    const val itemIdArg = "note_id"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    navController: NavController,
    navigateToNotesDisplay:() -> Unit,
    navigateBack: () -> Unit,
    navigateToHome:()->Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val incomingUiState = viewModel.editUiState.collectAsState()
    val uiState = viewModel.intermediateUiState
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    Log.d("INITIAL", uiState.intermediateNoteDetails.toString())

    if (uiState.intermediateNoteDetails.id == 0) {
        viewModel.initialise()
    }
    fun doDelete(note: NoteDetails) {
        Log.d("DELETE", note.title)
        coroutineScope.launch {
            viewModel.deleteNote(note)
            navigateToNotesDisplay()
        }
    }
    if (showDeleteDialog) DeleteNoteDialog(
        note = uiState.intermediateNoteDetails,
        onDismiss = { showDeleteDialog = !showDeleteDialog },
        onAccept = {
            showDeleteDialog = !showDeleteDialog
            doDelete(it)
        }
    )

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = incomingUiState.value.noteDetails.title,
                canNavigateBack = true,
                navigateUp = navigateBack,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDeleteDialog = !showDeleteDialog },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NoteEditBody(
            editUiState = uiState,
            onItemValueChange = viewModel::updateIntermediateUiState,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            onSaveClick = {
                coroutineScope.launch {
                    Log.d("SAVING", "Clciked")
                    Log.d("NEW ID", incomingUiState.value.noteDetails.id.toString())
                    Log.d("ID in", viewModel.editUiState.value.noteDetails.id.toString())
                    Log.d("ID Out", uiState.intermediateNoteDetails.id.toString())
                    viewModel.saveItem()
                    navigateBack()
                }
            },
        )
    }//end Scaffold

}//end NoteEditScreen

@Composable
fun NoteEditBody(
    editUiState: IntermediateUiState,
    modifier: Modifier,
    onSaveClick: () -> Unit,
    onItemValueChange: (NoteDetails) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .paint(painterResource(id = R.drawable.cobbles), contentScale = ContentScale.FillBounds)
    ) {
        Column(
            modifier = modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

            NoteEditDetails(
                note = editUiState.intermediateNoteDetails,
                onSaveClick = onSaveClick,
                onValueChange = onItemValueChange
            )
        }
    }
}//end NoteEditBody

@Composable
fun NoteEditDetails(
    note: NoteDetails,
    onSaveClick: () -> Unit,
    onValueChange: (NoteDetails) -> Unit,
) {
    NoteInputForm(
        noteDetails = note,
        onValueChange = onValueChange,
    )
    Button(
        onClick = onSaveClick,
        //enabled = noteUiState.isEntryValid,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Save", style = MaterialTheme.typography.bodyLarge, color = Color.LightGray)
    }
}
