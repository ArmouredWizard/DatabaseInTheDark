package uk.co.maddwarf.databaseinthedark.screens.scoundrel

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
import uk.co.maddwarf.databaseinthedark.model.ScoundrelDetails
import uk.co.maddwarf.databaseinthedark.model.toScoundrelDetails

class ScoundrelDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    val databaseInTheDarkRepository: DatabaseInTheDarkRepository
): ViewModel(){

    private val scoundrelId: Int = checkNotNull(savedStateHandle[ScoundrelDetailsDestination.itemIdArg])

    val detailsUiState: StateFlow<ScoundrelDetailsUiState> =
        databaseInTheDarkRepository.getScoundrelStream(scoundrelId)
            .filterNotNull()
            .map {
                ScoundrelDetailsUiState(scoundrelDetails = it.toScoundrelDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ScoundrelDetailsUiState()
            )

    val joinState:StateFlow<JoinState> =
        databaseInTheDarkRepository.getScoundrelWithContactsAndRank(scoundrelId)
            .filterNotNull()
            .map {
                JoinState(joined = it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = JoinState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}//end ViewModel

data class ScoundrelDetailsUiState(
    val scoundrelDetails: ScoundrelDetails = ScoundrelDetails()
)

data class JoinState(
    //val joined: List<ScoundrelWithContacts> = listOf()//ScoundrelWithContacts(scoundrel = ScoundrelDetails().toScoundrel(), contactsList = listOf())
    val joined: List<ContactAndRank> = listOf()//ScoundrelWithContacts(scoundrel = ScoundrelDetails().toScoundrel(), contactsList = listOf())
)

//todo add contactsdAndRank code