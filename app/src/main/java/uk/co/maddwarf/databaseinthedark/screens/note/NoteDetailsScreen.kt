package uk.co.maddwarf.databaseinthedark.screens.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.data.Note
import uk.co.maddwarf.databaseinthedark.model.toNote
import uk.co.maddwarf.databaseinthedark.screens.composables.BodyBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock


object NoteDetailsDestination : NavigationDestination {
    override val route = "note_details"
    override val titleRes: Int = R.string.scoundrel_detail_title

    const val itemIdArg = "note_id"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    navController: NavController,
    navigateBack: () -> Unit,
    navigateToNoteEdit: (Int) -> Unit,
    navigateToHome:()->Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = viewModel.detailsUiState.collectAsState()

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = uiState.value.noteDetails.title,
                canNavigateBack = true,
                navigateUp = navigateBack,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToNoteEdit(uiState.value.noteDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NoteDetailsBody(
            detailsUiState = uiState.value,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }//end Scaffold
}//end Details Screen

@Composable
fun NoteDetailsBody(
    detailsUiState: NoteDetailsUiState,
    modifier: Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .paint(painterResource(id = R.drawable.cobbles), contentScale = ContentScale.FillBounds)
    ) {
        //show details here
        Column(
            modifier = modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

            NoteDetails(
                note = detailsUiState.noteDetails.toNote(),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}//end detailsBody

@Composable
fun NoteDetails(
    note: Note,
    modifier: Modifier,

    ) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.End
    ) {

//todo NOTE DETAILS GO HERE. Maybe in a new Block
        TitleBlock(title = "Category", text = note.category)
        Spacer(Modifier.height(10.dp))
        BodyBlock(title = "", text = note.bodyText)

    }
}
