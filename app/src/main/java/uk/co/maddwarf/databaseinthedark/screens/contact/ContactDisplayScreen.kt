package uk.co.maddwarf.databaseinthedark.screens.contact

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.model.toContact
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.DeleteContactDialog

object ContactDisplayDestination : NavigationDestination {
    override val route = "contact_display"
    override val titleRes = R.string.contact_display_title
    const val contactIdArg = "contact_id"
    val routeWithArgs = "$route/{$contactIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDisplayScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToContactDetails: (Int) -> Unit,
    navigateToContactEntry: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ContactDisplayViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val displayUiState by viewModel.contactDisplayUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showDeleteContactDialog by remember { mutableStateOf(false) }
    var chosenContact by remember {
        mutableStateOf(
            ContactDetails().toContact()
        )
    }

    fun displayDeleteContactDialog(contact: Contact) {
        chosenContact = contact
        showDeleteContactDialog = true
    }

    fun doDelete(contact: Contact) {
        Log.d("DELETE IN UI", contact.name + " " + contact.contactId)
        coroutineScope.launch {
            viewModel.deleteContact(contact)
        }
    }

    if (showDeleteContactDialog) DeleteContactDialog(
        contact = chosenContact,
        onDismiss = { showDeleteContactDialog = !showDeleteContactDialog }
    ) {
        showDeleteContactDialog = !showDeleteContactDialog
        doDelete(it)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = "List of Contacts",
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToContactEntry,
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
        ContactDisplayBody(
            contactList = displayUiState.contactList,
            //     crewList = displayCrewUiState.crewList,
            onItemClick = navigateToContactDetails,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            displayDeleteContactDialog = { displayDeleteContactDialog(it) }
        )
    }
}//end ScoundrelDisplayScreen

@Composable
fun ContactDisplayBody(
    contactList: List<Contact>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier,
    displayDeleteContactDialog: (Contact) -> Unit
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

            if (contactList.isEmpty()) {
                Text(
                    text = "No Contacts Created Yet!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            } else {

                ContactList(
                    contactList = contactList,
                    onItemClick = {
                        Log.d("CLICK", it.contactId.toString())
                        onItemClick(it.contactId)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    displayDeleteContactDialog = displayDeleteContactDialog
                )
            }//end ELSE
        }//end Column
    }//end Box
}//end ContactDisplay Body

@Composable
private fun ContactList(
    contactList: List<Contact>,
    onItemClick: (Contact) -> Unit,
    modifier: Modifier = Modifier,
    displayDeleteContactDialog: (Contact) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = contactList, key = { it.contactId }) { item ->
            ContactItem(
                contact = item,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { onItemClick(item) },
                displayDeleteContactDialog = displayDeleteContactDialog
            )
        }
    }
}//end ContactList

@Composable
private fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier,
    displayDeleteContactDialog: (Contact) -> Unit
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
                    text = contact.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete",
                    Modifier
                        .clickable { displayDeleteContactDialog(contact) }
                        .align(Alignment.CenterVertically)
                        .size(25.dp)
                )
            }//end Row
        }//end column
    }//end Card
}//end Contact Item


