package uk.co.maddwarf.databaseinthedark.screens.note

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Note
import uk.co.maddwarf.databaseinthedark.model.NoteDetails
import uk.co.maddwarf.databaseinthedark.model.toNote
import uk.co.maddwarf.databaseinthedark.model.toNoteDetails
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.BodyBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.CustomSpinner
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteNoteDialog

object NoteDisplayDestination : NavigationDestination {
    override val route = "note_display"
    override val titleRes = R.string.note_display_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDisplayScreen(
    navController: NavController,
    navigateToNoteDetail: (Int) -> Unit,
    navigateToNoteEntry: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToHome:()->Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteDisplayViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val noteDisplayUiState by viewModel.noteDisplayUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var chosenNote by remember {
        mutableStateOf(
            Note(
                bodyText = "", id = 0, category = "", title = ""
            )
        )
    }

    fun displayDeleteDialog(note: Note) {
        chosenNote = note
        showDeleteDialog = true
    }

    fun doDelete(note: NoteDetails) {
        Log.d("DELETE", note.title)
        coroutineScope.launch {
            viewModel.deleteNote(note)
        }
    }

    if (showDeleteDialog) DeleteNoteDialog(
        note = chosenNote.toNoteDetails(),
        onDismiss = { showDeleteDialog = !showDeleteDialog },
        onAccept = {
            showDeleteDialog = !showDeleteDialog
            doDelete(it)
        }
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = "List of Notes",
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToNoteEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
    ) { innerPadding ->
        NoteDisplayBody(
            noteList = noteDisplayUiState.itemList,
            onItemClick = navigateToNoteDetail,
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            displayDeleteDialog = { displayDeleteDialog(it) }
        )
    }
}//end displayScreen

@Composable
private fun NoteDisplayBody(
    noteList: List<Note>,
    onItemClick: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    displayDeleteDialog: (Note) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .paint(painterResource(id = R.drawable.cobbles), contentScale = ContentScale.FillBounds)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(top = 5.dp)
        ) {
            if (noteList.isEmpty()) {
                Text(
                    text = "No Notes Created Yet!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            } else {

                var categoryList = mutableListOf("SHOW ALL")
                noteList.forEach {
                    categoryList.add(it.category)
                }
                categoryList = categoryList.distinct().toMutableList()


                var categoryExpanded by remember { mutableStateOf(false) }
                var chosenCategory by remember { mutableStateOf(categoryList[0]) }

                fun categoryChooser(categoryType: String) {
                    categoryExpanded = false
                    chosenCategory = categoryType
                }

                CustomSpinner(
                    //text = "Filter",
                    expanded = categoryExpanded,
                    onClick = { categoryExpanded = !categoryExpanded },
                    list = categoryList,
                    chooser = ::categoryChooser,
                    report = chosenCategory
                )

                var filteredList = noteList
                if (chosenCategory != "SHOW ALL") {
                    filteredList = noteList.filter { it.category == chosenCategory }
                }

                NoteList(
                    //noteList = noteList,
                    noteList = filteredList,
                    onItemClick = {
                        onItemClick(it.id)
                        //navController.navigate("note_details_screen/${it.id.toString()}")
                    },
                    modifier = Modifier.padding(horizontal = 6.dp),
                    displayDeleteDialog = displayDeleteDialog
                )
            }
        }
    }
}//end noteDisplayBody

@Composable
private fun NoteList(
    noteList: List<Note>,
    onItemClick: (Note) -> Unit,
    modifier: Modifier = Modifier,
    displayDeleteDialog: (Note) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = noteList) { item ->
            NoteItem(
                note = item,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { onItemClick(item) },
                displayDeleteDialog = displayDeleteDialog
            )

        }
    }
}//end NoteList


@Composable
private fun NoteItem(
    note: Note, modifier: Modifier = Modifier,
    displayDeleteDialog: (Note) -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete",
                    Modifier
                        .clickable { displayDeleteDialog(note) }
                        .align(Alignment.CenterVertically)
                        .size(25.dp)
                )
            }
            Text(
                text = "Category: ${note.category}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}//end NoteItem


