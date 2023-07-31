package uk.co.maddwarf.databaseinthedark.screens.scoundrel

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
import androidx.compose.runtime.setValue
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
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.model.Rank
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination

object ScoundrelEditDestination : NavigationDestination {
    override val route = "scoundrel_edit"
    override val titleRes = R.string.scoundrel_edit_title

    const val itemIdArg = "scoundrel_id"
    val routeWithArgs = "${route}/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoundrelEditScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToHome:()->Unit,
    viewModel: ScoundrelEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val incomingUiState = viewModel.editUiState.collectAsState()
    val uiState = viewModel.intermediateScoundrelUiState
    val editCrewUiState by viewModel.editCrewUiState.collectAsState()
    val scoundrelListFromRepoState by viewModel.scoundrelListFromRepoState.collectAsState()
    val contactListState by viewModel.contactListState.collectAsState()

    val scoundrelContactsState = viewModel.scoundrelContactsState.collectAsState()
    val contactList: List<ContactAndRank> = if (scoundrelContactsState.value.scoundrelContactsList.isNotEmpty()) {
        scoundrelContactsState.value.scoundrelContactsList
    } else {
        listOf()
    }
    val coroutineScope = rememberCoroutineScope()

    //???
    var loaded by remember{ mutableStateOf(false) }

    var playList = mutableListOf<String>()
    scoundrelListFromRepoState.scoundrelFromEditRepo.forEach {
        playList.add(it.playbook)
    }
    playList = playList.distinct().toMutableList()

    Log.d("INITIAL", uiState.intermediateScoundrelDetails.toString())

    var title = incomingUiState.value.scoundrelDetails.name

    var heritageList = mutableListOf<String>()
    scoundrelListFromRepoState.scoundrelFromEditRepo.forEach {
        heritageList.add(it.heritage)
    }
    heritageList = heritageList.distinct().toMutableList()

    var backgroundList = mutableListOf<String>()
    scoundrelListFromRepoState.scoundrelFromEditRepo.forEach {
        backgroundList.add(it.background)
    }
    backgroundList = backgroundList.distinct().toMutableList()

    var everyAbilityList = mutableListOf<String>()
    scoundrelListFromRepoState.scoundrelFromEditRepo.forEach{ scoundrel ->
        scoundrel.specialAbilities.forEach { ability ->
            if (!uiState.intermediateScoundrelDetails.specialAbilities.contains(ability)) {
                everyAbilityList.add(ability)
            }
        }
    }
    everyAbilityList = everyAbilityList.distinct().toMutableList()

    var everyContactList = mutableListOf<Contact>()
    contactListState.contactListFromRepo.forEach { contact ->
     //   if (!uiState.intermediateScoundrelDetails.contactsList.contains(contact))//todo ???
            everyContactList.add(contact)
    }
    everyContactList = everyContactList.distinct().toMutableList()

    if (uiState.intermediateScoundrelDetails.scoundrelId == 0) {
        viewModel.initialise()
        title = "New Scoundrel"
    }
    if (uiState.intermediateScoundrelDetails.scoundrelId!=0 && !loaded){
        viewModel.updateContacts()  //todo??????
        loaded = true
    }

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = title,
                canNavigateBack = true,
                navigateUp = navigateBack,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },//todo implement delete
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        ScoundrelEditBody(
            editUiState = uiState,
            onItemValueChange = viewModel::updateIntermediateUiState,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .paint(
                    painterResource(id = R.drawable.cobbles),
                    contentScale = ContentScale.FillBounds
                ),
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            crewList = editCrewUiState.crewList,
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
            doNewContact = viewModel::doNewContact,
            contactList = contactList
        )
    }//end Scaffold
}//end ScoundrelEditScreen

@Composable
fun ScoundrelEditBody(
    editUiState: IntermediateScoundrelUiState,
    modifier: Modifier,
    onSaveClick: () -> Unit,
    onItemValueChange: (ScoundrelDetails) -> Unit,
    crewList: List<Crew>,
    playbookList: List<String>,
    heritageList:List<String>,
    backgroundList:List<String>,
    everyAbilityList:List<String>,
    everyContactList:List<Contact>,
    saveContactConnection: (Contact, Rank)-> Unit,
    doNewContact:(String, Rank)->Unit,
    contactList: List<ContactAndRank>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        Column(
            modifier = modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          //  Text(text = contactList.toString())
            ScoundrelInputForm(
                scoundrelDetails = editUiState.intermediateScoundrelDetails,
                modifier = Modifier.fillMaxSize(),
                onValueChange = onItemValueChange,
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
                enabled = editUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(text = stringResource(R.string.save_action), style = MaterialTheme.typography.bodyLarge, color = Color.LightGray)
            }
        }
    }
}//end ScoundrelEditBody