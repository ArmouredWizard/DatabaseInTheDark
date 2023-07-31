package uk.co.maddwarf.databaseinthedark.screens.crew

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteCrewDialog

object CrewEditDestination : NavigationDestination {
    override val route = "crew_edit"
    override val titleRes: Int = R.string.crew_edit_title

    const val itemIdArg = "crew_id"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrewEditScreen(
    navController: NavController,
    navigateToCrewDisplay: () -> Unit,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CrewEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val incomingUiState = viewModel.crewEditUiState.collectAsState()
    val uiState = viewModel.intermediateCrewUiState
    val crewTypeState = viewModel.crewTypeState.collectAsState()
    val contactListState = viewModel.contactListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var loaded by remember { mutableStateOf(false) }

    var crewTypeList = mutableListOf<String>()
    crewTypeState.value.crewsFromRepo.forEach {
        crewTypeList.add(it.type)
    }
    crewTypeList = crewTypeList.distinct().toMutableList()

    var crewReputationList = mutableListOf<String>()
    crewTypeState.value.crewsFromRepo.forEach {
        crewReputationList.add(it.reputation)
    }
    crewReputationList = crewReputationList.distinct().toMutableList()

    var crewAbilitiesList = mutableListOf<String>()
    crewTypeState.value.crewsFromRepo.forEach { crew ->
        crew.crewAbilities.forEach {
            if (!uiState.intermediateCrewDetails.crewAbilities.contains(it))
                crewAbilitiesList.add(it)
        }
    }
    crewAbilitiesList = crewAbilitiesList.distinct().toMutableList()

    var everyContactList = mutableListOf<Contact>()
    contactListState.value.contactListFromRepo.forEach { contact ->
        //   if (!uiState.intermediateScoundrelDetails.contactsList.contains(contact))//todo ???
        everyContactList.add(contact)
    }
    everyContactList = everyContactList.distinct().toMutableList()

    var showDeleteDialog by remember { mutableStateOf(false) }

    Log.d("INITIAL", uiState.intermediateCrewDetails.toString())

    if (uiState.intermediateCrewDetails.name == "") {
        viewModel.initialise()
    }
    fun doDelete(crew: CrewDetails) {
        Log.d("DELETE", crew.name)
        coroutineScope.launch {
            viewModel.deleteCrew(crew)
            navigateToCrewDisplay()
        }
    }
    if (showDeleteDialog) DeleteCrewDialog(
        crew = uiState.intermediateCrewDetails,
        onDismiss = { showDeleteDialog = !showDeleteDialog },
        onAccept = {
            showDeleteDialog = !showDeleteDialog
            doDelete(it)
        }
    )

    if (uiState.intermediateCrewDetails.name!="" && !loaded){
        viewModel.updateContacts()  //todo??????
        loaded = true
    }

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = incomingUiState.value.crewDetails.name,
                canNavigateBack = true,
                navigateUp = navigateBack,
                navigateToHome = navigateToHome
            )
        },
        /*floatingActionButton = {
            FloatingActionButton(
                onClick = { showDeleteDialog = !showDeleteDialog },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon"
                )
            }
        },*/
        modifier = modifier
    ) { innerPadding ->
        CrewEditBody(
            editUiState = uiState,
            onItemValueChange = viewModel::updateIntermediateUiState,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            onSaveClick = {
                coroutineScope.launch {
                    Log.d("SAVING", "Clciked")
                    Log.d("NEW ID", incomingUiState.value.crewDetails.name)
                    Log.d("ID in", viewModel.crewEditUiState.value.crewDetails.name)
                    Log.d("ID Out", uiState.intermediateCrewDetails.name)
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            crewTypeList = crewTypeList,
            crewReputationList = crewReputationList,
            crewAbilitiesList = crewAbilitiesList,
            everyContactList = everyContactList,
            saveContactConnection =  {contact, rank ->
                coroutineScope.launch {
                    viewModel.updateContactsUiState(contact, rank)
                }
            }
        )
    }//end Scaffold

}//end CrewEditScreen

@Composable
fun CrewEditBody(
    editUiState: IntermediateCrewUiState,
    modifier: Modifier,
    onSaveClick: () -> Unit,
    onItemValueChange: (CrewDetails) -> Unit,
    crewTypeList: List<String>,
    crewReputationList: List<String>,
    crewAbilitiesList: List<String>,
    everyContactList:List<Contact>,
    saveContactConnection: (Contact, Rank)->Unit
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

            CrewEditDetails(
                crew = editUiState.intermediateCrewDetails,
                onSaveClick = onSaveClick,
                onValueChange = onItemValueChange,
                crewTypeList = crewTypeList,
                crewReputationList = crewReputationList,
                crewAbilitiesList = crewAbilitiesList,
                everyContactList = everyContactList,
                saveContactConnection = saveContactConnection
            )
        }
    }
}//end CrewEditBody

@Composable
fun CrewEditDetails(
    crew: CrewDetails,
    onSaveClick: () -> Unit,
    onValueChange: (CrewDetails) -> Unit,
    crewTypeList: List<String>,
    crewReputationList: List<String>,
    crewAbilitiesList: List<String>,
    everyContactList:List<Contact>,
    saveContactConnection: ( Contact, Rank) -> Unit
) {
    CrewInputForm(
        crewDetails = crew,
        onValueChange = onValueChange,
        crewTypeList = crewTypeList,
        crewReputationList = crewReputationList,
        everyAbilityList = crewAbilitiesList,
        everyContactList = everyContactList, //listOf(), //todo
        saveContactConnection = saveContactConnection //todo EDIT Crew Contacts not working

    )
    Button(
        onClick = onSaveClick,
        //enabled = crewUiState.isEntryValid,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Save", style = MaterialTheme.typography.bodyLarge, color = Color.LightGray)
    }
}