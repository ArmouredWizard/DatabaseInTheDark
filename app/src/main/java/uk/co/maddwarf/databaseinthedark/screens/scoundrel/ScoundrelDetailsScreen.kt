package uk.co.maddwarf.databaseinthedark.screens.scoundrel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.persistentListOf
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.model.toContact
import uk.co.maddwarf.databaseinthedark.model.toScoundrel
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.ActionBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton
import uk.co.maddwarf.databaseinthedark.screens.composables.TraitText
import uk.co.maddwarf.databaseinthedark.screens.composables.dialogs.InfoDialog
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.model.Rank

object ScoundrelDetailsDestination : NavigationDestination {
    override val route = "scoundrel_details"
    override val titleRes: Int = R.string.scoundrel_detail_title

    const val itemIdArg = "scoundrel_id"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoundrelDetailsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToScoundrelEdit: (Int) -> Unit,
    navigateToCrewDetails:(String) -> Unit,
    navigateToContactDetails:(Int)->Unit,
    viewModel: ScoundrelDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = viewModel.detailsUiState.collectAsState()
    val joinState = viewModel.joinState.collectAsState()
    val contactList: List<ContactAndRank> = joinState.value.joined.ifEmpty {
        listOf()
    }

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = uiState.value.scoundrelDetails.name,
                canNavigateBack = true,
                navigateUp = navigateBack,
                canNavigateHome = true,
                navigateToHome = navigateToHome
            )
        },
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick =
                { navigateToScoundrelEdit(uiState.value.scoundrelDetails.scoundrelId) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon"
                )
            }
        },
    ) { innerPadding ->
        ScoundrelDetailsBody(
            detailsUiState = uiState.value,
            contactList = contactList,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            navigateToCrewDetails = navigateToCrewDetails,
            navigateToContactDetails = navigateToContactDetails
        )
    }

}//end scoundrel details screen

@Composable
fun ScoundrelDetailsBody(
    detailsUiState: ScoundrelDetailsUiState,
    contactList: List<ContactAndRank>,
    modifier: Modifier,
    navigateToCrewDetails: (String) -> Unit,
    navigateToContactDetails:(Int)-> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .paint(painterResource(id = R.drawable.cobbles), contentScale = ContentScale.FillBounds)
    ) {
        //show details here
        Column(
            modifier = modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

            ScoundrelDetails(
                scoundrel = detailsUiState.scoundrelDetails.toScoundrel(),
                contactList = contactList,
                modifier = Modifier.fillMaxWidth(),
                navigateToCrewDetails = navigateToCrewDetails,
                navigateToContactDetails = navigateToContactDetails
            )
        }
    }
}//end detailsBody

@Composable
fun ScoundrelDetails(
    scoundrel: Scoundrel,
    contactList: List<ContactAndRank>,
    modifier: Modifier,
    navigateToCrewDetails: (String) -> Unit,
    navigateToContactDetails: (Int)->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TraitText(title = "Playbook: ", text = scoundrel.playbook)
            TraitText(title = "Crew: ", text = scoundrel.crew, onClick = navigateToCrewDetails)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TraitText(title = "Heritage: ", text = scoundrel.heritage)
            TraitText(title = "Background: ", text = scoundrel.background)
        }

        var showAttributesBlock by remember { mutableStateOf(false) }
        MyButton(onClick = { showAttributesBlock = !showAttributesBlock }, text = "Attributes", trailingIcon = Icons.Default.KeyboardArrowDown)

        if (showAttributesBlock) {
            Box(
                modifier = modifier
                    .fillMaxWidth(.9f)
                    .background(color = Color.DarkGray)
            ) {
                Column(
                    modifier = modifier
                        .padding(20.dp)
                ) {
                    ActionBlock(
                        attribute = "Insight",
                        actions = persistentListOf(
                            Pair("Hunt", scoundrel.hunt),
                            Pair("Study", scoundrel.study),
                            Pair("Survey", scoundrel.survey),
                            Pair("Tinker", scoundrel.tinker)
                        ),
                        rollable = true
                    )
                    ActionBlock(
                        attribute = "Prowess",
                        actions = persistentListOf(
                            Pair("Finesse", scoundrel.finesse),
                            Pair("Prowl", scoundrel.prowl),
                            Pair("Skirmish", scoundrel.skirmish),
                            Pair("Wreck", scoundrel.wreck)
                        ),
                        rollable = true
                    )
                    ActionBlock(
                        attribute = "Resolve",
                        actions = persistentListOf(
                            Pair("Attune", scoundrel.attune),
                            Pair("Command", scoundrel.command),
                            Pair("Consort", scoundrel.consort),
                            Pair("Sway", scoundrel.sway)
                        ),
                        rollable = true
                    )
                }//end Column
            }//end Box
        }//end IF ATTRIBUTES
        Spacer(Modifier.height(10.dp))

        var showAbilitiesBlock by remember { mutableStateOf(false) }
        MyButton(onClick = { showAbilitiesBlock = !showAbilitiesBlock }, text = "Special Abilities", trailingIcon = Icons.Default.KeyboardArrowDown)
        Spacer(modifier = Modifier.height(5.dp))
        if (showAbilitiesBlock) {
            scoundrel.specialAbilities.forEach {
                if (it != "") {
                    Spacer(modifier = Modifier.height(5.dp))
                    AbilityItem(
                        ability = it,
                        enableDelete = false,
                        displayDeleteDialog = {}
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }//end IF Abilities block

        var showContactsBlock by remember { mutableStateOf(false) }

        var showContactInfo by remember{ mutableStateOf(false) }
        var shownContactInfo by remember { mutableStateOf(ContactAndRank(ContactDetails().toContact(), Rank.NEUTRAL)) }
        fun doShowContact(contact: ContactAndRank){
            Log.d("CLICKED CONTATC", contact.contact.name)
            shownContactInfo = contact
            Log.d("SHOWN CONTACT", shownContactInfo.contact.name)
            showContactInfo = !showContactInfo
        }

        MyButton(onClick = { showContactsBlock = !showContactsBlock }, text = "Contacts", trailingIcon = Icons.Default.KeyboardArrowDown)
        Spacer(modifier = Modifier.height(5.dp))
        if (showContactsBlock) {
            contactList.forEach { it ->
                Spacer(modifier = Modifier.height(5.dp))
                ContactItem(
                    contact = it,
                    enableDelete = false,
                    onClick = { navigateToContactDetails(it.contact.contactId)}
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }//end IF Contacts block

        if(showContactInfo) InfoDialog(
            open = showContactInfo,
            onDismiss = { showContactInfo  = !showContactInfo},
            title = "Contact",
            body = shownContactInfo.contact.name
        )

    }//end Column
}