package uk.co.maddwarf.databaseinthedark.screens.crew

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
import androidx.compose.runtime.rememberCoroutineScope
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

object CrewEntryDestination : NavigationDestination {
    override val route = "crew_entry"
    override val titleRes = R.string.crew_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrewEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToHome:()->Unit,
    navController: NavController,
    viewModel: CrewEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
val crewListEntryState = viewModel.crewListEntryState.collectAsState()
    val crewContactsListState by viewModel.crewContactListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var crewTypeList = mutableListOf<String>()
    crewListEntryState.value.crewsFromRepo.forEach {
        crewTypeList.add(it.type)
    }
    crewTypeList = crewTypeList.distinct().toMutableList()

    val crewReputationList = mutableListOf<String>()
    crewListEntryState.value.crewsFromRepo.forEach {
        crewReputationList.add(it.reputation)
    }
    var crewAbilitiesList = mutableListOf<String>()
    crewListEntryState.value.crewsFromRepo.forEach { crew ->
        crew.crewAbilities.forEach {
            if (!viewModel.crewEntryUiState.crewDetails.crewAbilities.contains(it))
                crewAbilitiesList.add(it)
        }
    }
    crewAbilitiesList = crewAbilitiesList.distinct().toMutableList()

    var everyContactList = mutableListOf<Contact>()
    crewContactsListState.contactsFromRepo.forEach { contact ->
        //  if (!uiState.scoundrelDetails.contactsList.first().contains(contact))
        everyContactList.add(contact) //todo
    }
    everyContactList = everyContactList.distinct().toMutableList()

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = "Enter Crew Details Here",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateToHome = navigateToHome
            )
        }
    ) { innerPadding ->
        CrewEntryBody(
            crewUiState = viewModel.crewEntryUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            crewTypeList = crewTypeList,
            crewReputationList = crewReputationList,
            crewAbilitiesList = crewAbilitiesList,
            everyContactList = everyContactList,
            saveContactConnection = {contact, rank ->
                coroutineScope.launch {
                    viewModel.updateContactsUiState(contact, rank)
                }
            },
            doNewContact =  {p1,p2 -> Unit}  //viewModel::doNewContact

        )
    }
}//end CrewEntryScreen

@Composable
fun CrewEntryBody(
    crewUiState: CrewEntryUiState,
    crewTypeList:List<String>,
    crewReputationList:List<String>,
    crewAbilitiesList:List<String>,
    everyContactList:List<Contact>,
    onItemValueChange: (CrewDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    saveContactConnection:(Contact, Rank)->Unit,
    doNewContact:(String, Rank)->Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .paint(painterResource(id = R.drawable.cobbles), contentScale = ContentScale.FillBounds)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(10.dp)
        ) {
            CrewInputForm(
                crewDetails = crewUiState.crewDetails,
                onValueChange = onItemValueChange,
                crewTypeList = crewTypeList, //todo fet from ViewModel and repo
                crewReputationList = crewReputationList,
                everyAbilityList = crewAbilitiesList,
                everyContactList = everyContactList,
                saveContactConnection = saveContactConnection,
              //  doNewContact = doNewContact
            )
            Button(
                onClick = onSaveClick,
                enabled = crewUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save", style = MaterialTheme.typography.bodyLarge, color = Color.LightGray)
            }
        }
    }
}//end NoteEntryBody