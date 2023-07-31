package uk.co.maddwarf.databaseinthedark.screens.crew

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.model.toCrew
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.BodyBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton
import uk.co.maddwarf.databaseinthedark.screens.composables.TraitDots
import uk.co.maddwarf.databaseinthedark.screens.composables.TraitText

object CrewDetailsDestination : NavigationDestination {
    override val route = "crew_details"
    override val titleRes: Int = R.string.crew_detail_title

    const val itemIdArg = "crew_id"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrewDetailsScreen(
    navController: NavController,
    navigateBack: () -> Unit,
    navigateToCrewEdit: (String) -> Unit,
    navigateToScoundrelDetails: (Int) -> Unit,
    navigateToContactDetails: (Int) -> Unit,
    navigateToCrewEntry: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CrewDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = viewModel.crewDetailsUiState.collectAsState()
    val crewDetailsWithScoundrelsUiState =
        viewModel.crewDetailsWithScoundrelsUiState.collectAsState()
    val scoundrelList = crewDetailsWithScoundrelsUiState.value.crewWithScoundrelList.scoundrels
    val contactListState = viewModel.contactsWithRankUiState.collectAsState()
    val contactList = contactListState.value.crewWithContacts

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = uiState.value.crewDetails.name,
                canNavigateBack = true,
                navigateUp = navigateBack,
                navigateToHome = navigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToCrewEdit(uiState.value.crewDetails.name) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        CrewDetailsBody(
            detailsUiState = uiState.value,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            scoundrelist = scoundrelList,
            contactList = contactList,
            onScoundrelClick = navigateToScoundrelDetails,
            onContactClick = navigateToContactDetails,
        )
    }//end Scaffold
}//end Details Screen

@Composable
fun CrewDetailsBody(
    detailsUiState: CrewDetailsUiState,
    modifier: Modifier,
    onScoundrelClick: (Int) -> Unit,
    onContactClick: (Int) -> Unit,
    scoundrelist: List<Scoundrel>,
    contactList: List<ContactAndRank>
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

            CrewDetailsBlock(
                crew = detailsUiState.crewDetails.toCrew(),
                modifier = Modifier.fillMaxSize(),
                scoundrelList = scoundrelist,
                onScoundrelClick = {
                    // Log.d("CLICK", it.id.toString())
                    onScoundrelClick(it.scoundrelId)
                },
                onContactClick = { onContactClick(it.contact.contactId) },
                contactList = contactList
            )
        }
    }
}//end detailsBody

@Composable
fun CrewDetailsBlock(
    crew: Crew,
    modifier: Modifier,
    scoundrelList: List<Scoundrel>,
    contactList: List<ContactAndRank>,
    onScoundrelClick: (Scoundrel) -> Unit,
    onContactClick: (ContactAndRank) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TraitText(title = "Type: ", text = crew.type)
            TraitText(title = "Reputation: ", crew.reputation)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TraitText(title = "HQ: ", text = crew.hq)
            TraitText(title = "Hunting Grounds: ", crew.huntingGrounds)
        }

        Spacer(modifier = Modifier.height(10.dp))

        val strong = if (crew.holdIsStrong) {
            "Strong"
        } else {
            "Weak"
        }
        TraitDots(
            traitName = "Rep",
            traitValue = crew.rep.toInt(),
            maxValue = 12,
            onDotClicked = {},
            infoText = stringResource(R.string.reputation_info)
        )
        Spacer(modifier = Modifier.height(10.dp))
        TraitDots(
            traitName = "Heat",
            traitValue = crew.heat.toInt(),
            maxValue = 6,
            onDotClicked = {},
            infoText = stringResource(R.string.heat_info)
        )
        Spacer(modifier = Modifier.height(10.dp))

        TraitDots(
            traitName = "Tier",
            traitValue = crew.tier.toInt(),
            maxValue = 4,
            onDotClicked = {},
            infoText = stringResource(R.string.tier_info)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Hold: $strong",
            modifier = Modifier
                .background(color = Color.LightGray)
                .clip(shape = RoundedCornerShape(10.dp))
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        var abilityExpanded by remember { mutableStateOf(false) }
        MyButton(
            onClick = { abilityExpanded = !abilityExpanded },
            text = "Crew Abilities",
            trailingIcon = Icons.Default.KeyboardArrowDown
        )
        if (abilityExpanded) {
            crew.crewAbilities.forEach {
                BodyBlock(text = it)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }

        var scoundrelExpanded by remember { mutableStateOf(false) }
        MyButton(
            onClick = { scoundrelExpanded = !scoundrelExpanded },
            text = "Scoundrels",
            trailingIcon = Icons.Default.KeyboardArrowDown
        )
        if (scoundrelExpanded) {
            // LazyColumn(modifier = modifier.height(500.dp)) {
            //    items(items = crewList, key = { it.name }) { item ->
            scoundrelList.forEach {
                CrewScoundrelItem(
                    scoundrel = it,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onScoundrelClick(it) },
                    displayDeleteDialog = false //displayDeleteDialog
                )
            }//end LazyColumn
        }//end Scoundrel IF

        var contactExpanded by remember { mutableStateOf(false) }
        MyButton(
            onClick = { contactExpanded = !contactExpanded },
            text = "Contacts",
            trailingIcon = Icons.Default.KeyboardArrowDown
        )
        if (contactExpanded) {
            contactList.forEach {
                CrewContactItem(
                    contactAndRank = it,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onContactClick(it) }
                )
            }
        }

    }//end Column
}//end CrewDetailsBlock

@Composable
fun CrewScoundrelItem(
    scoundrel: Scoundrel,
    modifier: Modifier,
    displayDeleteDialog: Boolean
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
            Text(
                text = scoundrel.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Playbook: ${scoundrel.playbook}", //todo fix
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}//end CrewScoundrelItem

@Composable
fun CrewContactItem(
    contactAndRank: ContactAndRank,
    modifier: Modifier
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
            Text(
                text = contactAndRank.contact.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Relationship: ${contactAndRank.rank.rankName}", //todo fix
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}//end CrewContactItem