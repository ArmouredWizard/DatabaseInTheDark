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
import uk.co.maddwarf.databaseinthedark.model.NoteDetails
import uk.co.maddwarf.databaseinthedark.model.toNoteDetails

class NoteDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    databaseInTheDarkRepository: DatabaseInTheDarkRepository,
) : ViewModel() {

    private val noteId: Int = checkNotNull(savedStateHandle[NoteDetailsDestination.itemIdArg])

    val detailsUiState: StateFlow<NoteDetailsUiState> =
        databaseInTheDarkRepository.getNoteStream(noteId)
            .filterNotNull()
            .map {
                NoteDetailsUiState(noteDetails = it.toNoteDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class NoteDetailsUiState(
    val noteDetails: NoteDetails = NoteDetails()
)