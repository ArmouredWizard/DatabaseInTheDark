package uk.co.maddwarf.databaseinthedark.screens.crew

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.toCrew
import uk.co.maddwarf.databaseinthedark.model.toCrewDetails
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.CustomSpinner
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteCrewDialog

object CrewDisplayDestination : NavigationDestination {
    override val route = "crew_display"
    override val titleRes = R.string.crew_display_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrewDisplayScreen(
    navController: NavController,
    navigateToCrewDetails: (Int) -> Unit,
    navigateToCrewEntry: () -> Unit,
    navigateToHome: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CrewDisplayViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val crewDisplayUiState by viewModel.crewDisplayUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var chosenCrew by remember { mutableStateOf(CrewDetails().toCrew()) }

    fun displayDeleteDialog(crew: Crew) {
        chosenCrew = crew
        showDeleteDialog = true
    }

    fun doDelete(crew: CrewDetails) {
        Log.d("DELETE", crew.name)
        coroutineScope.launch {
            viewModel.deleteCrew(crew)
        }
    }

    if (showDeleteDialog) DeleteCrewDialog(
        crew = chosenCrew.toCrewDetails(),
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
                title = "List of Crews",
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCrewEntry,
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
        CrewDisplayBody(
            crewList = crewDisplayUiState.itemList,
            onItemClick = navigateToCrewDetails,
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            displayDeleteDialog = { displayDeleteDialog(it) }
        )
    }//end Scaffold

}//end CrewDisplayScreen

@Composable
private fun CrewDisplayBody(
    crewList: List<Crew>,
    onItemClick: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    displayDeleteDialog: (Crew) -> Unit
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
            if (crewList.isEmpty()) {
                Text(
                    text = "No Crews Created Yet!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            } else {

                var categoryList = mutableListOf("SHOW ALL")
                crewList.forEach {
                    categoryList.add(it.type)
                }
                categoryList = categoryList.distinct().toMutableList()

                var categoryExpanded by remember { mutableStateOf(false) }
                var chosenCategory by remember { mutableStateOf(categoryList[0]) }

                fun categoryChooser(categoryType: String) {
                    categoryExpanded = false
                    chosenCategory = categoryType
                }
                CustomSpinner(
                    expanded = categoryExpanded,
                    onClick = { categoryExpanded = !categoryExpanded },
                    list = categoryList,
                    chooser = ::categoryChooser,
                    report = chosenCategory
                )
                var filteredList = crewList
                if (chosenCategory != "SHOW ALL") {
                    filteredList = crewList.filter { it.type == chosenCategory } //todo swap to Type
                }

                CrewList(
                    crewList = filteredList,
                    onItemClick = {
                        onItemClick(it.crewId)
                    },
                    modifier = Modifier.padding(horizontal = 6.dp),
                    displayDeleteDialog = displayDeleteDialog
                )
            }
        }
    }
}//end crewDisplayBody

@Composable
private fun CrewList(
    crewList: List<Crew>,
    onItemClick: (Crew) -> Unit,
    modifier: Modifier = Modifier,
    displayDeleteDialog: (Crew) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = crewList, key = { it.crewId }) { item ->
            CrewItem(
                crew = item,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { onItemClick(item) },
                displayDeleteDialog = displayDeleteDialog
            )
        }
    }
}//end CrewList

@Composable
fun CrewItem(
    crew: Crew,
    modifier: Modifier = Modifier,
    displayDeleteDialog: (Crew) -> Unit
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
                    text = crew.crewName,
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete",
                    Modifier
                        .clickable { displayDeleteDialog(crew) }
                        .align(Alignment.CenterVertically)
                        .size(25.dp)
                )
            }
            Text(
                text = "Type: ${crew.type}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}//end crewItem