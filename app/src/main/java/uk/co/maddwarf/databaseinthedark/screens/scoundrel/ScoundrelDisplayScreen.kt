package uk.co.maddwarf.databaseinthedark.screens.scoundrel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toScoundrel
import uk.co.maddwarf.databaseinthedark.screens.composables.CustomSpinner
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteScoundrelDialog

object ScoundrelDisplayDestination : NavigationDestination {
    override val route = "scoundrel_display"
    override val titleRes = R.string.scoundrel_display_title
    const val scoundrelIdArg = "scoundrel_id"
    val routeWithArgs = "$route/{$scoundrelIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoundrelDisplayScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToScoundrelDetails: (Int) -> Unit,
    navigateToScoundrelEntry: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ScoundrelDisplayViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val displayUiState by viewModel.scoundrelDisplayUiState.collectAsState()
    val displayCrewUiState by viewModel.displayCrewUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showDeleteScoundrelDialog by remember { mutableStateOf(false) }
    var chosenScoundrel by remember {
        mutableStateOf(
            ScoundrelDetails().toScoundrel()
        )
    }

    fun displayDeleteScoundrelDialog(scoundrel: Scoundrel) {
        chosenScoundrel = scoundrel
        showDeleteScoundrelDialog = true
    }

    fun doDelete(scoundrel: Scoundrel) {
        coroutineScope.launch {
            viewModel.deleteScoundrel(scoundrel)
        }
    }

    if (showDeleteScoundrelDialog) DeleteScoundrelDialog(
        scoundrel = chosenScoundrel,
        onDismiss = { showDeleteScoundrelDialog = !showDeleteScoundrelDialog }
    ) {
        showDeleteScoundrelDialog = !showDeleteScoundrelDialog
        doDelete(it)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = "List of Scoundrels",
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToScoundrelEntry,
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
        ScoundrelDisplayBody(
            scoundrelList = displayUiState.scoundrelList,
            crewList = displayCrewUiState.crewList,
            onItemClick = navigateToScoundrelDetails,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            displayDeleteScoundrelDialog = { displayDeleteScoundrelDialog(it) }
        )
    }
}//end ScoundrelDisplayScreen

@Composable
fun ScoundrelDisplayBody(
    scoundrelList: List<Scoundrel>,
    crewList: List<Crew>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier,
    displayDeleteScoundrelDialog: (Scoundrel) -> Unit
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
        ) {

            if (scoundrelList.isEmpty()) {
                Text(text = "NO SCOUNDRELS")
            } else {
                var playbookList = mutableListOf("ALL PLAYBOOKS")
                scoundrelList.forEach { playbookList.add(it.playbook) }
                playbookList = playbookList.distinct().toMutableList()

                var playbookExpanded by remember { mutableStateOf(false) }
                var chosenPlaybook by remember { mutableStateOf(playbookList[0]) }

                fun playbookChooser(playbookType: String) {
                    playbookExpanded = false
                    chosenPlaybook = playbookType
                    Log.d("Playbook ", chosenPlaybook)
                }

                var playbookFilteredList = scoundrelList
                var crewFilteredList = playbookFilteredList

                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomSpinner(
                        expanded = playbookExpanded,
                        onClick = { playbookExpanded = !playbookExpanded },
                        list = playbookList,
                        chooser = ::playbookChooser,
                        report = chosenPlaybook,
                    )
                    if (chosenPlaybook != "ALL PLAYBOOKS") {
                        playbookFilteredList =
                            scoundrelList.filter { it.playbook == chosenPlaybook }
                    }

                    var crewNamesList = mutableListOf("ALL CREWS")
                    crewList.forEach { crewNamesList.add(it.crewName) }
                    crewNamesList.add("No Crew")
                    crewNamesList = crewNamesList.distinct().toMutableList()

                    var crewExpanded by remember { mutableStateOf(false) }
                    var chosenCrew by remember { mutableStateOf(crewNamesList[0]) }

                    fun crewChooser(crewType: String) {
                        crewExpanded = false
                        chosenCrew = crewType
                        Log.d("Crew ", chosenCrew)
                    }

                    CustomSpinner(
                        expanded = crewExpanded,
                        onClick = { crewExpanded = !crewExpanded },
                        list = crewNamesList,
                        chooser = ::crewChooser,
                        report = chosenCrew
                    ) ///TODO swap to Crew Spinner ?????

                    crewFilteredList = if (chosenCrew == "ALL CREWS") {
                        playbookFilteredList
                    } else {
                        playbookFilteredList.filter { it.crew == chosenCrew }
                    }
                }  //todo CREW FILTER
                ScoundrelList(
                    scoundrelList = crewFilteredList,
                    onItemClick = {
                        Log.d("CLICK", it.scoundrelId.toString())
                        Log.d("ABILITY", it.specialAbilities.size.toString())
                        onItemClick(it.scoundrelId)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    displayDeleteScoundrelDialog = displayDeleteScoundrelDialog
                )

            }
        }//end column
    }//end box
}//end Display Nody

@Composable
private fun ScoundrelList(
    scoundrelList: List<Scoundrel>,
    onItemClick: (Scoundrel) -> Unit,
    modifier: Modifier = Modifier,
    displayDeleteScoundrelDialog: (Scoundrel) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = scoundrelList, key = { it.scoundrelId }) { item ->
            ScoundrelItem(
                scoundrel = item,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { onItemClick(item) },
                displayDeleteScoundrelDialog = displayDeleteScoundrelDialog
            )
        }
    }
}//end ScoundrelList

@Composable
private fun ScoundrelItem(
    scoundrel: Scoundrel,
    modifier: Modifier = Modifier,
    displayDeleteScoundrelDialog: (Scoundrel) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = scoundrel.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete",
                    Modifier
                        .clickable { displayDeleteScoundrelDialog(scoundrel) }
                        .align(Alignment.CenterVertically)
                        .size(25.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Playbook: ${scoundrel.playbook}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(text = "Crew: ${scoundrel.crew}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

