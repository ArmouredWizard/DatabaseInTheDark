package uk.co.maddwarf.databaseinthedark.screens.scoundrel

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination

object ScoundrelEntryDestination : NavigationDestination {
    override val route = "scoundrel_entry"
    override val titleRes = R.string.scoundrel_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoundrelEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToHome:()->Unit,
    viewModel: ScoundrelEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    //define lists, vals and funs here
    val uiState = viewModel.scoundrelEntryUiState
    val crewUiState by viewModel.crewUiState.collectAsState()
    val scoundrelListState by viewModel.scoundrelListState.collectAsState()
    val contactListState by viewModel.contactListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var playList = mutableListOf<String>()
    scoundrelListState.scoundrelListFromRepo.forEach {
        playList.add(it.playbook)
    }
    playList = playList.distinct().toMutableList()

    var heritageList = mutableListOf<String>()
    scoundrelListState.scoundrelListFromRepo.forEach {
        heritageList.add(it.heritage)
    }
    heritageList = heritageList.distinct().toMutableList()

    var backgroundList = mutableListOf<String>()
    scoundrelListState.scoundrelListFromRepo.forEach {
        backgroundList.add(it.background)
    }
    backgroundList = backgroundList.distinct().toMutableList()

    var everyAbilityList = mutableListOf<String>()
    scoundrelListState.scoundrelListFromRepo.forEach { scoundrel ->
        scoundrel.specialAbilities.forEach { ability ->
            if (!uiState.scoundrelDetails.specialAbilities.contains(ability))
                everyAbilityList.add(ability)
        }
    }
    everyAbilityList = everyAbilityList.distinct().toMutableList()

    var everyContactList = mutableListOf<Contact>()
    contactListState.contactListFromRepo.forEach { contact ->
          //  if (!uiState.scoundrelDetails.contactsList.first().contains(contact))
                everyContactList.add(contact) //todo
    }
    everyContactList = everyContactList.distinct().toMutableList()

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = stringResource(ScoundrelEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateToHome = navigateToHome
            )
        }
    ) { innerPadding ->
        ScoundrelEntryBody(
            entryUiState = uiState,
            onValueChange = viewModel::updateUiStateEntry,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .paint(
                    painterResource(id = R.drawable.cobbles),
                    contentScale = ContentScale.FillBounds
                ),
            crewList = crewUiState.crewList,
            playbookList = playList,
            heritageList = heritageList,
            backgroundList = backgroundList,
            everyAbilityList = everyAbilityList,
            everyContactList = everyContactList,
            saveContactConnection = {contact, rank ->
                coroutineScope.launch {
                    viewModel.updateContactsUiState(contact, rank)
                }
            },
            doNewContact = viewModel::doNewContact
        )
    }//end scaffold
}//end Scoundrel Entry Screen

@Composable
fun ScoundrelEntryBody(
    entryUiState: ScoundrelEntryUiState,
    onValueChange: (ScoundrelDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    crewList: List<Crew>,
    playbookList: List<String>,
    heritageList: List<String>,
    backgroundList: List<String>,
    everyAbilityList: List<String>,
    everyContactList:List<Contact>,
    saveContactConnection: (Contact, Rank)-> Unit,
    doNewContact:(String, Rank)->Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier.padding(8.dp)
        ) {
            ScoundrelInputForm(
                scoundrelDetails = entryUiState.scoundrelDetails,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                crewList = crewList,
                playbookList = playbookList,
                heritageList = heritageList,
                backgroundList = backgroundList,
                everyAbilityList = everyAbilityList,
                everyContactList = everyContactList,
                saveContactConnection = saveContactConnection,
                doNewContact = doNewContact
            )
            Button(
                onClick = onSaveClick,
                enabled = entryUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(text = stringResource(R.string.save_action))
            }
        }
    }
}//end Scoundrel Entry BODY