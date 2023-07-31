package uk.co.maddwarf.databaseinthedark.screens.contact

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
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination

object ContactEntryDestination : NavigationDestination {
    override val route = "contact_entry"
    override val titleRes = R.string.contact_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToHome:()->Unit,
    viewModel: ContactEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    //STATES, vals and functions go here
    val uiState = viewModel.contactEntryUiState



    val coroutineScope = rememberCoroutineScope()



    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = stringResource(ContactEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                navigateToHome = navigateToHome
            )
        }
    ) { innerPadding ->
        ContactEntryBody(
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
                ,

        )
    }//end scaffold

}//end ContactEntryScreen

@Composable
fun ContactEntryBody(
    entryUiState:ContactEntryUiState,
    onValueChange :(ContactDetails)-> Unit,
    onSaveClick:()->Unit,
    modifier: Modifier,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .paint(
                painterResource(id = R.drawable.cobbles),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier.padding(8.dp)
        ) {
            ContactInputForm(
                contactDetails = entryUiState.contactDetails,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
/*                crewList = crewList,
                playbookList = playbookList,
                heritageList = heritageList,
                backgroundList = backgroundList,
                everyAbilityList = everyAbilityList,
                everyContactList = everyContactList,*/
             //   saveContactConnection = saveContactConnection,
             //   doNewContact = doNewContact
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

        }//end column
    }//end Box
}//end Contact Entry Body