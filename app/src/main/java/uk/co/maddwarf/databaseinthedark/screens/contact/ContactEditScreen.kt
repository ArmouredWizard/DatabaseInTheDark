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
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEditViewModel

object ContactEditDestination : NavigationDestination {
    override val route = "contact_edit"
    override val titleRes = R.string.contact_edit_title

    const val itemIdArg = "contact_id"
    val routeWithArgs = "${route}/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactEditScreen(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToHome: () -> Unit,
    viewModel: ContactEditViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    var editUiState = viewModel.editUiState.collectAsState()
    var uiState = viewModel.intermediateContactUiState

    val coroutineScope = rememberCoroutineScope()

    var title = editUiState.value.contactDetails.name


    var loaded by remember { mutableStateOf(false) } //?? load crwes and scoundrels??????

    if (uiState.intermediateContactDetails.contactId == 0) {
        viewModel.initialise()
        title = "New Scoundrel"
    }

    fun onSaveClick() {
        coroutineScope.launch {
            viewModel.saveItem()
            navigateBack()
        }
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
                onClick = { },//todo implement delete
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon"
                )
            }
        },
        modifier = Modifier
    ) { innerPadding ->
        ContactEditBody(
            modifier = Modifier
                .padding(innerPadding)
               // .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            contactDetails = editUiState.value.contactDetails,
            onSaveClick = { onSaveClick() },
            onItemValueChange = viewModel::updateIntermediateUiState,
            editUiState = uiState
        )
//stuff goers here
    }//end innerPadding content

}//end contactEditScreen

@Composable
fun ContactEditBody(
    modifier: Modifier,
    contactDetails: ContactDetails,
    onItemValueChange: (ContactDetails) -> Unit,
    onSaveClick: () -> Unit,
    editUiState: IntermediateContactUiState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .paint(
                painterResource(id = R.drawable.cobbles),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier.padding(8.dp)
        ) {
            ContactInputForm(
                modifier = modifier
                    .fillMaxWidth(),
                onValueChange = onItemValueChange,//TODO
                contactDetails = editUiState.intermediateContactDetails//contactDetails
            )
            Button(
                onClick = onSaveClick,
                enabled = editUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(
                    text = stringResource(R.string.save_action),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray
                )
            }
        }
    }
}