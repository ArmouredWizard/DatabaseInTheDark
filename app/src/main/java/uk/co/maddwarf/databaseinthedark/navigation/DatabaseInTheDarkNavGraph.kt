package uk.co.maddwarf.databaseinthedark.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uk.co.maddwarf.databaseinthedark.home.HomeDestination
import uk.co.maddwarf.databaseinthedark.home.HomeScreen
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactDetailsDestination
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactDetailsScreen
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactDisplayDestination
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactDisplayScreen
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactEditDestination
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactEditScreen
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactEntryDestination
import uk.co.maddwarf.databaseinthedark.screens.contact.ContactEntryScreen
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewDetailsDestination
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewDetailsScreen
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewDisplayDestination
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewDisplayScreen
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewEditDestination
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewEditScreen
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewEntryDestination
import uk.co.maddwarf.databaseinthedark.screens.crew.CrewEntryScreen
import uk.co.maddwarf.databaseinthedark.screens.note.NoteDetailsDestination
import uk.co.maddwarf.databaseinthedark.screens.note.NoteDetailsScreen
import uk.co.maddwarf.databaseinthedark.screens.note.NoteDisplayDestination
import uk.co.maddwarf.databaseinthedark.screens.note.NoteDisplayScreen
import uk.co.maddwarf.databaseinthedark.screens.note.NoteEditDestination
import uk.co.maddwarf.databaseinthedark.screens.note.NoteEditScreen
import uk.co.maddwarf.databaseinthedark.screens.note.NoteEntryDestination
import uk.co.maddwarf.databaseinthedark.screens.note.NoteEntryScreen
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsDestination
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsScreen
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDisplayDestination
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDisplayScreen
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEditDestination
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEditScreen
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEntryDestination
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelEntryScreen

@Composable
fun DatabaseInTheDarkNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                modifier = Modifier,
                navigateToScoundrelDisplay = { navController.navigate(ScoundrelDisplayDestination.route) },
                navigateToContactsDisplay = { navController.navigate(ContactDisplayDestination.route) },
                navigateToCrewDisplay = { navController.navigate(CrewDisplayDestination.route) },
                navigateToSettings = {},//todo add Settings destination
                navigateToNoteDisplay = { navController.navigate(NoteDisplayDestination.route) }
            )
        }//end Home composable
        composable(route = ScoundrelDisplayDestination.route) {
            ScoundrelDisplayScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                onNavigateUp = { navController.navigateUp() },
                navigateToScoundrelDetails = { navController.navigate("${ScoundrelDetailsDestination.route}/${it}") },
                navigateToScoundrelEntry = { navController.navigate(ScoundrelEntryDestination.route) }
            )
        }//end scoundrelDisplay composable
        composable(
            route = ScoundrelDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ScoundrelDetailsDestination.itemIdArg) {
                    type = NavType.IntType
                })
        ) {
            ScoundrelDetailsScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateBack = { navController.popBackStack() },
                navigateToScoundrelEdit = { navController.navigate("${ScoundrelEditDestination.route}/${it}") },
                navigateToCrewDetails = { navController.navigate("${CrewDetailsDestination.route}/${it}") },
                navigateToContactDetails = { navController.navigate("${ContactDetailsDestination.route}/${it}") },

                )
        }//end ScoundrelDetailsScreen
        composable(route = ScoundrelEntryDestination.route) {
            ScoundrelEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }//end scoundrelEntryScreen
        composable(route = ScoundrelEditDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ScoundrelEditDestination.itemIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            ScoundrelEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }//end scoundrel Edit screen

        composable(route = ContactEntryDestination.route) {
            ContactEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }//end Contact Entry Screen

        composable(route = ContactDisplayDestination.route) {
            ContactDisplayScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
                navigateToContactDetails = { navController.navigate("${ContactDetailsDestination.route}/${it}") },
                navigateToContactEntry = { navController.navigate(ContactEntryDestination.route) }
            )
        }//end ContactDisplayScreen
        composable(route = ContactDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ContactDetailsDestination.itemIdArg) {
                    type = NavType.IntType
                }
            )) {
            ContactDetailsScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateBack = { navController.popBackStack() },
                navigateToContactEdit = { navController.navigate( "${ContactEditDestination.route}/${it}") },
                navigateToScoundrelDetails = { navController.navigate("${ScoundrelDetailsDestination.route}/${it}") },
                navigateToCrewDetails = { navController.navigate("${CrewDetailsDestination.route}/${it}") }
                )
        }//end ContactDetailsScreen


        composable(route = CrewDisplayDestination.route) {
            CrewDisplayScreen(
                navController = navController,
                navigateToCrewDetails = { navController.navigate("${CrewDetailsDestination.route}/${it}") },
                navigateToCrewEntry = { navController.navigate(CrewEntryDestination.route) },
                onNavigateUp = { navController.navigateUp() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }//end CrewDisplayScreen
        composable(route = CrewEntryDestination.route) {
            CrewEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
                navController = navController
            )
        }//end CrewEntryScreen
        composable(
            route = CrewDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(CrewDetailsDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            CrewDetailsScreen(
                navController = navController,
                navigateBack = { navController.popBackStack() },
                navigateToCrewEdit = { navController.navigate("${CrewEditDestination.route}/${it}") },
                navigateToScoundrelDetails = { navController.navigate("${ScoundrelDetailsDestination.route}/${it}") },
                navigateToContactDetails = { navController.navigate("${ContactDetailsDestination.route}/${it}") },
                navigateToCrewEntry = { navController.navigate(CrewEntryDestination.route) },
                navigateToHome = { navController.navigate(HomeDestination.route) })
        }//end DrewDetailScreen
        composable(route = CrewEditDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CrewEditDestination.itemIdArg) {
                    type = NavType.StringType
                }
            )) {
            CrewEditScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
                navigateToCrewDisplay = { navController.navigate(CrewDisplayDestination.route) },
                navController = navController
            )
        }


        composable(route = ContactEditDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ContactEditDestination.itemIdArg){
                    type = NavType.IntType
                }
            )
        ){
            ContactEditScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
            )
        }



        composable(route = NoteDisplayDestination.route) {
            NoteDisplayScreen(
                navController = navController,
                navigateToNoteDetail = { navController.navigate("${NoteDetailsDestination.route}/${it}") }, //todo
                navigateToNoteEntry = { navController.navigate(NoteEntryDestination.route) },
                onNavigateUp = { navController.popBackStack() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                })
        }
        composable(route = NoteEntryDestination.route) {
            NoteEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
                navController = navController
            )
        }
        composable(route = NoteDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(NoteDetailsDestination.itemIdArg) {
                    type = NavType.IntType
                }
            )) {
            NoteDetailsScreen(
                navController = navController,
                navigateBack = { navController.popBackStack() },
                navigateToNoteEdit = { navController.navigate("${NoteEditDestination.route}/${it}") },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }
        composable(NoteEditDestination.routeWithArgs,
            arguments = listOf(
                navArgument(NoteEditDestination.itemIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            NoteEditScreen(
                navController = navController,
                navigateToNotesDisplay = { navController.navigate(NoteDisplayDestination.route) },
                navigateBack = { navController.popBackStack() },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }

    }//end NavHost
}//end DitDNavHost