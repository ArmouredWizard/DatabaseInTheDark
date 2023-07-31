package uk.co.maddwarf.databaseinthedark.screens.scoundrel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.data.Scoundrel

class ScoundrelDisplayViewModel(val databaseInTheDarkRepository: DatabaseInTheDarkRepository): ViewModel(){
    val scoundrelDisplayUiState: StateFlow<ScoundrelDisplayUiState> =
        databaseInTheDarkRepository.getAllScoundrelsStream().map{ ScoundrelDisplayUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ScoundrelDisplayUiState()
            )

    val displayCrewUiState: StateFlow<DisplayCrewUiState> =
        databaseInTheDarkRepository.getAllCrewsStream().map { DisplayCrewUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DisplayCrewUiState()
            )


    suspend fun deleteScoundrel(scoundrel: Scoundrel){
        databaseInTheDarkRepository.removeContactScoundrelLink(scoundrel.scoundrelId)
        databaseInTheDarkRepository.deleteScoundrel(scoundrel)
        //todo delete crew/scouncrel link?
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}//end viewModel

data class ScoundrelDisplayUiState(
    val scoundrelList: List<Scoundrel> = listOf(),
)

data class DisplayCrewUiState(
    val crewList: List<Crew> = listOf()
)