package uk.co.maddwarf.databaseinthedark.screens.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.maddwarf.databaseinthedark.data.DatabaseInTheDarkRepository
import uk.co.maddwarf.databaseinthedark.data.Note
import uk.co.maddwarf.databaseinthedark.model.NoteDetails
import uk.co.maddwarf.databaseinthedark.model.toNote
import uk.co.maddwarf.databaseinthedark.model.toNoteDetails

class NoteDisplayViewModel(val databaseInTheDarkRepository: DatabaseInTheDarkRepository): ViewModel() {
    val noteDisplayUiState: StateFlow<NoteDisplayUiState> =
        databaseInTheDarkRepository.getAllNotesStream().map{ NoteDisplayUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteDisplayUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteNote(note: NoteDetails){
        databaseInTheDarkRepository.deleteNote(note.toNote())
    }
}//end viewModel

data class NoteDisplayUiState(val itemList: List<Note> = listOf())