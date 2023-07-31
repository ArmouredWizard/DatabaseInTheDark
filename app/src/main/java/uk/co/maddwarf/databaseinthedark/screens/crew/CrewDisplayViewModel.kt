package uk.co.maddwarf.databaseinthedark.screens.crew

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.Crew
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.model.CrewDetails
import uk.co.maddwarf.databaseinthedark.model.toCrew


class CrewDisplayViewModel(val databaseInTheDarkRepository: DatabaseInTheDarkRepository): ViewModel() {
    val crewDisplayUiState: StateFlow<CrewDisplayUiState> =
        databaseInTheDarkRepository.getAllCrewsStream().map{ CrewDisplayUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CrewDisplayUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteCrew(crew: CrewDetails){
        Log.d("DELETE from DISPLAY", crew.name)
        val scoundrelsToProcess = databaseInTheDarkRepository.getScoundrelsByCrew(crew.name)
        Log.d("TO DELETE", scoundrelsToProcess.toString())
        scoundrelsToProcess.forEach {scoundrel ->
            databaseInTheDarkRepository.updateScoundrel(scoundrel.copy(crew = "No Crew"))
        }
        databaseInTheDarkRepository.deleteCrew(crew.toCrew())
    }
}//end viewModel

data class CrewDisplayUiState(val itemList: List<Crew> = listOf())