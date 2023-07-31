package uk.co.maddwarf.databaseinthedark.screens.crew

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.data.relations.ContactAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.CrewWithContacts
import uk.co.maddwarf.databaseinthedark.data.relations.CrewWithScoundrels
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.toCrew
import uk.co.maddwarf.databaseinthedark.model.toCrewDetails

class CrewDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    databaseInTheDarkRepository: DatabaseInTheDarkRepository,
) : ViewModel() {

    private val crewId: String = checkNotNull(savedStateHandle[CrewDetailsDestination.itemIdArg])

    val crewDetailsUiState: StateFlow<CrewDetailsUiState> =
        databaseInTheDarkRepository.getCrewStream(crewId)
            .filterNotNull()
            .map {
                CrewDetailsUiState(crewDetails = it.toCrewDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewDetailsUiState()
            )
    val crewDetailsWithScoundrelsUiState: StateFlow<CrewDetailsWithScoundrelsUiState> =
        databaseInTheDarkRepository.getCrewWithScoundrels(crewId)
            .map {
                CrewDetailsWithScoundrelsUiState(crewWithScoundrelList = it[0])
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewDetailsWithScoundrelsUiState()
            )

    val contactsWithRankUiState:StateFlow<ContactsWithRankUiState> =
        databaseInTheDarkRepository.getCrewWithContactsAndRank(crewId)
            .map {
                ContactsWithRankUiState(crewWithContacts = it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContactsWithRankUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CrewDetailsUiState(
    val crewDetails: CrewDetails = CrewDetails()
)

data class CrewDetailsWithScoundrelsUiState(
    val crewWithScoundrelList: CrewWithScoundrels = CrewWithScoundrels(
        crew = CrewDetails().toCrew(),
        scoundrels = listOf()
    )
)

data class ContactsWithRankUiState(
    val crewWithContacts: List<ContactAndRank> = listOf()
)