package uk.co.maddwarf.databaseinthedark.screens.contact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.data.Scoundrel
import uk.co.maddwarf.databaseinthedark.data.relations.ContactWithScoundrels
import uk.co.maddwarf.databaseinthedark.data.relations.CrewAndRank
import uk.co.maddwarf.databaseinthedark.data.relations.ScoundrelAndRank
import uk.co.maddwarf.databaseinthedark.model.ContactDetails
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toContactDetails
import uk.co.maddwarf.databaseinthedark.model.toScoundrelDetails
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsDestination
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsUiState
import uk.co.maddwarf.databaseinthedark.screens.scoundrel.ScoundrelDetailsViewModel

class ContactDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    val databaseInTheDarkRepository: DatabaseInTheDarkRepository
): ViewModel() {

    private val contactId: Int = checkNotNull(savedStateHandle[ContactDetailsDestination.itemIdArg])

    val detailsUiState: StateFlow<ContactDetailsUiState> =
        databaseInTheDarkRepository.getContactStream(contactId)
            .filterNotNull()
            .map {
                ContactDetailsUiState(contactDetails = it.toContactDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContactDetailsUiState()
            )

    val scoundrelsUiState: StateFlow<ScoundrelsUiState> =
        databaseInTheDarkRepository.getContactWithScoundrelAndRank(contactId)
            .filterNotNull()
            .map {
                ScoundrelsUiState(scoundrelsList = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ScoundrelsUiState()
            )

    val crewsUiState:StateFlow<CrewsUiState> =
        databaseInTheDarkRepository.getContactWithCrewAndRank(contactId)
            .filterNotNull()
            .map {
                CrewsUiState(crewsList = it)
            }.stateIn(
                scope = viewModelScope,
                started =  SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewsUiState()
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}//end viewModel

data class ContactDetailsUiState(
    val contactDetails: ContactDetails = ContactDetails()
)

data class ScoundrelsUiState(
    val scoundrelsList: List<ScoundrelAndRank> = listOf()
)

data class CrewsUiState(
    val crewsList: List<CrewAndRank> = listOf()
)