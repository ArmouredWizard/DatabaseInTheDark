package uk.co.maddwarf.databaseinthedark.screens.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.maddwarf.databaseinthedark.AppViewModelProvider
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.data.Contact
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.data.relations.ContactWithScoundrels
import uk.co.maddwarf.databaseinthedark.data.relations.CrewAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.ScoundrelAndRank
import uk.co.maddwarf.databaseinthedark.model.toContact
import uk.co.maddwarf.databaseinthedark.model.toScoundrel
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.TitleBlock
import uk.co.maddwarf.databaseinthedark.screens.composables.TraitText
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsBody
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsUiState
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsViewModel

object ContactDetailsDestination : NavigationDestination {
    override val route = "contact_details"
    override val titleRes: Int = R.string.contact_detail_title

    const val itemIdArg = "contact_id"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToContactEdit: (Int) -> Unit,
    navigateToScoundrelDetails: (Int) -> Unit,
    navigateToCrewDetails: (Int)->Unit,
    viewModel: ContactDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),

    ) {
    val uiState = viewModel.detailsUiState.collectAsState()
    val scoundrelsUiState = viewModel.scoundrelsUiState.collectAsState()
    val crewsUiState = viewModel.crewsUiState.collectAsState()

    Scaffold(
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = uiState.value.contactDetails.name,
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
                { navigateToContactEdit(uiState.value.contactDetails.contactId) },
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
        ContactDetailsBody(
            detailsUiState = uiState.value,
            //  contactList = contactList,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
              navigateToScoundrelDetails =  navigateToScoundrelDetails,
            navigateToCrewDetails = navigateToCrewDetails,
            scoundrelList = if (scoundrelsUiState.value.scoundrelsList.isNotEmpty()) {
                scoundrelsUiState.value.scoundrelsList
            } else {
                listOf()
            },
            crewList = if (crewsUiState.value.crewsList.isNotEmpty()) {
                crewsUiState.value.crewsList
            } else {
                listOf()
            }
        )
    }//end scaffold

}//end Contact Details Screen

@Composable
fun ContactDetailsBody(
    detailsUiState: ContactDetailsUiState,
    //  contactList: List<Contact>,
    modifier: Modifier,
     navigateToScoundrelDetails: (Int) -> Unit,
    navigateToCrewDetails:(Int)->Unit,
    scoundrelList: List<ScoundrelAndRank>,
    crewList:List<CrewAndRank>
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

            ContactDetails(
                contact = detailsUiState.contactDetails.toContact(),
                //  contactList = contactList,
                modifier = Modifier.fillMaxWidth(),
                navigateToScoundrelDetails = navigateToScoundrelDetails,
                navigateToCrewDetails = navigateToCrewDetails,
                scoundrelList = scoundrelList,
                crewList = crewList
            )

        }
    }
}//end detailsBody

@Composable
fun ContactDetails(
    contact: Contact,
    modifier: Modifier,
       navigateToScoundrelDetails: (Int) -> Unit,
    navigateToCrewDetails:(Int)->Unit,
    scoundrelList: List<ScoundrelAndRank>,
    crewList:List<CrewAndRank>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            TraitText(title = "SOME DETAILS", text = "Details about ${contact.name} go here")
        }
        ScoundrelList(
            scoundrelList = scoundrelList,
            navigateToScoundrelDetails = navigateToScoundrelDetails
        )
        Spacer(modifier = Modifier.height(6.dp))
        CrewList(
            crewList = crewList,
            navigateToCrewDetails = navigateToCrewDetails
        )
    }
}//end ContactDetails

@Composable
fun ScoundrelList(
    scoundrelList: List<ScoundrelAndRank>,
    navigateToScoundrelDetails:(Int)->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TitleBlock(title = "", text = "Known Scoundrels")
        scoundrelList.forEach {
            ScoundrelRankItem(
                scoundrel = it,
                onClick = navigateToScoundrelDetails
                )
        }
    }
}//end scoundrel List

@Composable
fun ScoundrelRankItem(
    scoundrel: ScoundrelAndRank,
    onClick:(Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(5.dp)
            .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.LightGray)
                .padding(10.dp)
                .fillMaxWidth()
                .clickable { onClick(scoundrel.scoundrel.scoundrelId) },
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = scoundrel.scoundrel.name)
            Text(text = scoundrel.rank.rankName)
        }
    }
}

@Composable
fun CrewList(
    crewList:List<CrewAndRank>,
    navigateToCrewDetails:(Int) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TitleBlock(title = "", text = "Known Crews")
        crewList.forEach {
            CrewRankItem(
                crew = it,
                onClick = navigateToCrewDetails
            )
        }
    }
}

@Composable
fun CrewRankItem(
    crew: CrewAndRank,
    onClick:(Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(5.dp)
            .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.LightGray)
                .padding(10.dp)
                .fillMaxWidth()
                .clickable { onClick(crew.crew.crewId) },
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = crew.crew.crewName)
            Text(text = crew.rank.rankName)
        }
    }
}
