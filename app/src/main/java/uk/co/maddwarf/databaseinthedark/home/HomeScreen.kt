package uk.co.maddwarf.databaseinthedark.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.maddwarf.databaseinthedark.DatabaseInTheDarkTopAppBar
import uk.co.maddwarf.databaseinthedark.R
import uk.co.maddwarf.databaseinthedark.navigation.NavigationDestination
import uk.co.maddwarf.databaseinthedark.screens.composables.MyButton

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navigateToScoundrelDisplay:()->Unit,
    navigateToContactsDisplay:()->Unit,
    navigateToCrewDisplay: () -> Unit,
    navigateToSettings: ()->Unit,
    navigateToNoteDisplay:()->Unit
){

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DatabaseInTheDarkTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                canNavigateHome = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick =  navigateToSettings ,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon"
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navigateToScoundrelDisplay = navigateToScoundrelDisplay,
            navigateToContactsDisplay = navigateToContactsDisplay,
            navigateToCrewDisplay = navigateToCrewDisplay,
            navigateToNoteDisplay = navigateToNoteDisplay
        )
    }//end Scaffold
}//end HomeScreen

@Composable
fun HomeBody(
    modifier: Modifier,
    navigateToScoundrelDisplay: ()-> Unit,
    navigateToContactsDisplay:()->Unit,
    navigateToCrewDisplay:()->Unit,
    navigateToNoteDisplay:()->Unit
){
    Box(
        modifier = modifier
            .background(color = Color.LightGray)
            .paint(painterResource(id = R.drawable.cobbles), contentScale = ContentScale.FillBounds)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            MyButton(
                text = "Scoundrels",
                onClick = navigateToScoundrelDisplay,
            )
            MyButton(
                text = "Notes",
                onClick = navigateToNoteDisplay,
            )
            MyButton(
                onClick = navigateToCrewDisplay,
                text = "Crew View"
            )
            MyButton(
                onClick = navigateToContactsDisplay,
                text = "Contacts"
            )
        }//end Column
    }//end Box
}